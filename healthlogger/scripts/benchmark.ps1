
param(
    [ValidateRange(1, 100000)]
    [int]$N = 10
)

$ErrorActionPreference = "Stop"

$Port = 8088
$Store = "data/benchmark-events.jsonl"
$ServerLog = "data/benchmark-server.log"
$ServerErrorLog = "data/benchmark-server-error.log"
$Url = "http://127.0.0.1:$Port"
$ServerProcess = $null

if (!(Test-Path ".\pom.xml") -or !(Test-Path ".\config\client-spec.json")) {
    throw "Run this script from the HealthLogger project root."
}

$Maven = (Get-Command mvn -ErrorAction Stop).Source

Remove-Item $Store, $ServerLog, $ServerErrorLog `
    -Force -ErrorAction SilentlyContinue

try {
    Write-Host "Starting HealthLogger server..."

    $Arguments = @(
        "-q"
        "exec:java"
        '"-Dexec.args=serve --store data/benchmark-events.jsonl"'
    )

    $ServerProcess = Start-Process `
        -FilePath $Maven `
        -ArgumentList $Arguments `
        -WorkingDirectory (Get-Location).Path `
        -RedirectStandardOutput $ServerLog `
        -RedirectStandardError $ServerErrorLog `
        -PassThru

    Write-Host "Waiting for server..."
    $Ready = $false

    for ($i = 0; $i -lt 30; $i++) {
        if ($ServerProcess.HasExited) {
            throw "Server exited early. Check $ServerLog and $ServerErrorLog."
        }

        try {
            $Health = Invoke-WebRequest `
                -Uri "$Url/health" `
                -Method Get `
                -TimeoutSec 2 `
                -UseBasicParsing

            if ([int]$Health.StatusCode -eq 200) {
                $Ready = $true
                break
            }
        }
        catch {
            # Retry while the server starts.
        }

        Start-Sleep -Milliseconds 500
    }

    if (!$Ready) {
        throw "Server did not become ready. Check the server logs."
    }

    Write-Host "Server ready. Sending $N events..."

    $Watch = [System.Diagnostics.Stopwatch]::StartNew()

    for ($i = 1; $i -le $N; $i++) {
        $Event = @{
            service   = "benchmark-api"
            status    = "ok"
            timestamp = "2026-10-16T00:00:00Z"
        } | ConvertTo-Json -Compress

        try {
            $Response = Invoke-WebRequest `
                -Uri "$Url/events" `
                -Method Post `
                -ContentType "application/json" `
                -Body $Event `
                -TimeoutSec 10 `
                -UseBasicParsing

            if ([int]$Response.StatusCode -ne 201) {
                throw "Expected HTTP 201, received HTTP $($Response.StatusCode)."
            }
        }
        catch {
            throw "Event $i failed: $($_.Exception.Message)"
        }
    }

    $Watch.Stop()
    $Elapsed = $Watch.Elapsed.TotalSeconds
    $Rate = $N / $Elapsed

    $Accepted = 0
    if (Test-Path $Store) {
        $Accepted = @(Get-Content $Store).Count
    }

    Write-Host ""
    Write-Host "Benchmark complete."
    Write-Host "Requested events: $N"
    Write-Host "Accepted events:  $Accepted"
    Write-Host ("Elapsed time:     {0:N3} seconds" -f $Elapsed)
    Write-Host ("Events/second:    {0:N2}" -f $Rate)

    if ($Accepted -ne $N) {
        throw "Expected $N stored events but found $Accepted."
    }

    Write-Host "PASS: all events received HTTP 201 and were stored."
}
finally {
    if ($ServerProcess -and !$ServerProcess.HasExited) {
        & taskkill.exe /PID $ServerProcess.Id /T /F 2>$null | Out-Null
    }
}
