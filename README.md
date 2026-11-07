# Scale Connector

[![Tests](https://github.com/PakLer123/scale-connector/actions/workflows/tests.yml/badge.svg)](https://github.com/PakLer123/scale-connector/actions/workflows/tests.yml)

A Java desktop app that reads live weight data from industrial scales (and other
RS-232 sensors) over a serial port and serves it over the network, so that an
ERP like [iDempiere](https://www.idempiere.org/) can record weights automatically
when goods are received or shipped.

<!-- TODO: add a screenshot or GIF of the app here, e.g. docs/screenshot.png -->

> **About this project:** this repository extends the open-source
> [Scale Connector](https://wiki.idempiere.org/en/Plugin:_Scale_Connector)
> by INGEINT (2015, GPL-2.0). I modernized and extended it during my
> internship in January 2025, and later upgraded it to Java 25. The changes I
> made are listed below.

## What I built

- **Live data streaming:** a toggleable live feed of scale readings, replacing
  the original one-shot read.
- **Byte-to-ASCII layer:** an `AsciiTranslator` and an ASCII view of the raw
  serial frames, which makes it much easier to debug unfamiliar scale protocols.
- **Better parsing and stability detection:** configurable start/end characters
  and cut positions, a stability indicator, and a warning when no data is read.
- **Hardware simulator (`SCSimulator`):** a Swing app that emulates a scale on a
  serial port, so the connector can be developed and tested without physical
  hardware.
- **Modernized build:** moved from Eclipse/JSSC/Java 11 to Gradle, Java 25 and
  [jSerialComm](https://fazecast.github.io/jSerialComm/), with a fat-jar build.
- **Safer, lighter TCP server:** one virtual thread per connection, a
  deserialization filter that only accepts the protocol's own classes (blocking
  Java deserialization attacks), and fixes for leaked sockets and a shutdown
  race.
- **New UI:** [FlatLaf](https://www.formdev.com/flatlaf/) look and feel with dark
  mode.
- **Testing and CI:** JUnit and Mockito tests for the serial-port reading and
  parsing logic (using a mocked port), plus end-to-end client/server tests, run
  on every push by GitHub Actions.
- **Team workflow:** each feature was tracked as a GitHub issue and merged
  through a pull request.

## How it works

```
 ┌──────────┐  RS-232   ┌──────────────────────┐   TCP    ┌──────────────┐
 │  Scale / │ ────────▶ │   Scale Connector    │ ───────▶ │  Client mode │
 │  sensor  │  serial   │    (server mode)     │  :5000   │  or the ERP  │
 └──────────┘           │ parse → stabilize →  │          │              │
                        │      serve value     │          └──────────────┘
                        └──────────────────────┘
```

The connector runs in one of two modes:

- **Server mode:** opens the serial port, reads frames between the configured
  start and end characters, extracts the weight, checks it is stable across
  several readings, and answers requests over TCP.
- **Client mode:** connects to a running server and shows its readings.

Start and end characters can be entered as a character code (`2`), a control
character name (`STX`) or a single literal character (`+`). The default is
STX/ETX, which most scales use.

## Project structure

| Folder | Description |
| --- | --- |
| `GradleScaleConnector/` | The main application (GUI and console, server and client modes) |
| `SCSimulator/` | Scale simulator for testing without hardware |

## Getting started

Requirements: a JDK (17 or newer) to run Gradle. Gradle downloads Java 25 for
the build automatically.

```bash
# Run the tests
cd GradleScaleConnector
./gradlew test

# Start the connector
./gradlew run

# Build a standalone jar (build/libs/)
./gradlew jar
```

### Testing without a scale

1. Create a pair of linked virtual serial ports, for example on Linux:
   `socat -d -d pty,raw,echo=0 pty,raw,echo=0`
2. Start the simulator (`cd SCSimulator && ./gradlew run`) on one port.
3. Start the connector in server mode on the other port.

### Linux serial port permissions

```bash
ls -l /dev/ttyS* /dev/ttyUSB*
sudo gpasswd -a $USER dialout   # Debian/Ubuntu/Fedora (uucp on Arch)
```

## Credits and license

Based on [Scale Connector](https://wiki.idempiere.org/en/Plugin:_Scale_Connector),
Copyright © 2015 [INGEINT](http://www.ingeint.com), originally by Saúl Piña.
Modifications © 2025 Derrel Tedianto.

Licensed under the GNU General Public License v2.0. See [COPYING](COPYING).
