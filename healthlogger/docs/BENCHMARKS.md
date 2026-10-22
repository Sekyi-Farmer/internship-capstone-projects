# HealthLogger Benchmark Results

## 1. Purpose

This document records benchmark results for the HealthLogger HTTP API.

The benchmark measures how long HealthLogger takes to process a specified number of valid HTTP `POST /events` requests.

The benchmark is intended to provide a simple, repeatable measurement of the current application's performance in the development environment.

---

## 2. Benchmark Method

The benchmark is run using:

```text
scripts/benchmark.sh
```

The script:

1. Starts the HealthLogger HTTP server.
2. Waits for the `/health` endpoint to become available.
3. Sends a specified number of valid `POST /events` requests.
4. Measures the total elapsed time for the requests.
5. Calculates events processed per second.
6. Stops the HealthLogger server.

The number of events can be supplied as an argument.

Example:

```bash
./scripts/benchmark.sh 100
```

---

## 3. Benchmark Environment

The benchmark was performed on the development computer using:

* Operating system: Windows
* Java: Temurin OpenJDK 21.0.12.1 LTS
* HealthLogger: Java 21 application
* Storage: JSONL file
* HTTP server: JDK `HttpServer`
* Benchmark shell: Git Bash
* HTTP client: curl
* Storage file used by the benchmark: `data/benchmark-events.jsonl`

The benchmark was run locally against:

```text
http://127.0.0.1:8088
```

---

## 4. Results

The following measurements were obtained from the development environment.

| Events |    Elapsed Time | Events/Second |
| -----: | --------------: | ------------: |
|    100 |  20.572 seconds |          4.86 |
|    500 | 100.300 seconds |          4.99 |
|  1,000 | 198.217 seconds |          5.04 |

---

## 5. Observations

The measured throughput remained close to 5 events per second across the three benchmark sizes.

The measured results were:

```text
100 events    → 4.86 events/second
500 events    → 4.99 events/second
1000 events   → 5.04 events/second
```

The results show that the benchmark produced similar throughput as the number of events increased from 100 to 1,000.

---

## 6. Limitations

These measurements describe the current development environment and should not be treated as a general performance limit for HealthLogger.

The results can be affected by:

* Computer hardware
* Operating system
* Java runtime
* Background processes
* File-system performance
* JSON serialization
* JSONL storage
* Local HTTP processing

The benchmark was performed locally and does not represent performance over a production network or under concurrent client load.

The benchmark also measures the complete HTTP request and JSONL storage path, rather than isolating individual components.

---

## 7. Reproducing the Benchmark

From the HealthLogger project root, run:

```bash
./scripts/benchmark.sh 100
```

For a larger test:

```bash
./scripts/benchmark.sh 500
```

or:

```bash
./scripts/benchmark.sh 1000
```

The script prints the number of events, elapsed time, and calculated events per second.

---

## 8. Summary

The Week 5 benchmark provides a repeatable way to measure the current HealthLogger HTTP event-recording performance.

The recorded development-environment measurements were approximately 4.86 to 5.04 events per second for workloads of 100 to 1,000 events.

---

## 9. Windows PowerShell Benchmark

A second benchmark was performed using the PowerShell script:

```text
scripts/benchmark.ps1
```

Unlike the original Bash benchmark, this script uses PowerShell's `Invoke-WebRequest` to send HTTP requests and verify that each request returns HTTP `201 Created`.
The script also checks that the number of stored JSONL records matches the number of requested events.

### 9.1 Results

The following historical measurements were obtained using the PowerShell benchmark script.

| Events | Elapsed Time   | Events/Second |
| -----: | -----------:   | ------------: |
| 10     | 1.855 seconds  | 5.39          |
| 10     | 1.511 seconds  | 6.62          |
| 100    | 9.000 seconds  | 11.11         |
| 500    | 24.378 seconds | 20.51         |
| 1,000  | 38.639 seconds | 25.88         |

All five historical runs completed successfully. Each run reported that every requested event received HTTP `201` and was stored. The two 10-event runs produced different measurements, illustrating that short benchmarks can be affected by startup conditions and measurement variability.

### 9.1.1 Latest PowerShell Benchmark Results

These measurements were collected on 22 October 2026 using the Windows development computer.

| Events | Elapsed Time   | Events/Second | Result |
| -----: | -----------:   | ------------: | :----- |
| 10     | 0.674 seconds  | 14.85         | PASS   |
| 100    | 3.137 seconds  | 31.88         | PASS   |
| 1,000  | 29.824 seconds | 33.53         | PASS   |

All three runs completed successfully. Every requested event received HTTP `201 Created`, and the expected number of events was stored in the benchmark JSONL file.

Throughput increased across these three measurements. However, each workload was measured only once in this session, so the results are preliminary. Repeated runs under consistent conditions are needed to assess variability and establish a more reliable performance baseline.

These results were collected using the Windows PowerShell benchmark script and should be interpreted separately from the Bash benchmark measurements.
### 9.2 Reproducing the PowerShell Benchmark

Run these commands from the HealthLogger project root in PowerShell:

```powershell
.\scripts\benchmark.ps1 -N 10
.\scripts\benchmark.ps1 -N 100
.\scripts\benchmark.ps1 -N 500
.\scripts\benchmark.ps1 -N 1000
```

The `-N` argument specifies the number of events to send. The script writes benchmark records and logs to the benchmark-specific files under `data/`.

### 9.3 Interpretation

These measurements were obtained using a different benchmark script and HTTP client from the original Git Bash benchmark. The results should therefore be treated as separate measurements, not as a direct comparison proving a performance improvement.

Requests are sent sequentially. These results do not measure concurrent-client performance or establish a production capacity limit.
