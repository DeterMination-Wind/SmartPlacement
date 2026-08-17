# Smart Placement

[中文](README_zh.md) | [English](README.md)

Smart Placement is a small Mindustry Java mod that improves dragged item-line intersections. On mixed-tech maps, perpendicular item transport lines automatically use an unconfigured inverted sorter instead of a junction or bridge.

## Behavior

Mixed-tech placement is enabled when the current rules use `Planets.sun`. In the rules dialog, this is the **Any** planet option.

The following perpendicular crossings are supported, including subclasses such as armored conveyors and armored ducts:

| Newly dragged line | Existing line | Result |
|---|---|---|
| Conveyor | Conveyor | Unconfigured inverted sorter |
| Conveyor | Duct | Unconfigured inverted sorter |
| Duct | Conveyor | Unconfigured inverted sorter |
| Duct | Duct | Unconfigured inverted sorter |
| Stack conveyor | Any supported item line | Unconfigured inverted sorter |

On every single-tech planet, Smart Placement leaves line placement untouched, including vanilla junction and bridge generation. Parallel lines, endpoints, and non-item obstacles also retain vanilla behavior.

## Installation

Download the universal JAR from [Releases](https://github.com/DeterMination-Wind/SmartPlacement/releases) and place it in Mindustry's `mods/` directory. The same JAR contains desktop bytecode and Android `classes.dex`.

Requires Mindustry build 157 or newer. The mod supports vanilla Mindustry and MindustryX.

## Build

```powershell
gradlew clean build
```

Outputs:

- `dist/SmartPlacement.jar` - universal installable mod
- `dist/SmartPlacement.zip` - JAR with English and Chinese documentation
- `../构建/SmartPlacement/SmartPlacement-v1.0.0.jar`
- `../构建/SmartPlacement/SmartPlacement-v1.0.0.zip`

The Android dexing step requires D8 and `android.jar`. Set `D8_PATH`, `ANDROID_SDK_ROOT`, or `ANDROID_HOME` when they are not available from the workspace toolchain.
