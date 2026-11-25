# Smart Placement

> Make crossed item lines behave as expected, with less trial and error.

Smart Placement is a small Mindustry client-side helper for mixed-tech maps. When two item lines cross, it chooses the appropriate unconfigured crossing solution instead of leaving you to repair an accidental junction or bridge.

It is useful on maps where multiple technology sets share one construction space. Normal placement remains vanilla in situations that are not ambiguous, so the mod stays out of the way when no help is needed.

## What it does

- Crossing two lines places an unconfigured inverted sorter, so both lines keep running instead of overwriting each other.
- Works for conveyor-type lines (conveyor, titanium conveyor, plastanium conveyor, armored conveyor) and duct-type lines (duct, armored duct), including mixed pairs.
- When a duct line runs into a band of 3 or more crossing item lines, it jumps over the whole band with an item duct bridge; 1–2 tiles still get a sorter.
- Parallel lines, line endpoints, non-item obstacles and single-tech planets keep vanilla behavior.

## Settings

| Setting | Default | Effect |
| --- | --- | --- |
| Conveyor × conveyor | on | Sorter crossing for conveyor-type over conveyor-type |
| Conveyor × duct | on | Sorter crossing for conveyor-type over duct-type |
| Duct × conveyor | on | Sorter crossing for duct-type over conveyor-type |
| Duct × duct | on | Sorter crossing for duct-type over duct-type |
| Duct bridge over wide crossings | on | Bands wider than 2 tiles are bridged instead of overwritten |
| Only on mixed-tech planets | on | Act only when the map planet is "Any" |

## Installation

Download the universal JAR from [Releases](https://github.com/DeterMination-Wind/SmartPlacement/releases) and place it in Mindustry's mods directory. The JAR supports desktop and Android.

Mindustry 157 or newer is required. The mod works with vanilla Mindustry and MindustryX.

## Build

~~~powershell
.\gradlew.bat clean build
~~~

## 中文

# 智能放置

> 让混合科技地图里的交叉运输线更符合直觉，减少误放后的返工。

智能放置是一个轻量的 Mindustry 客户端辅助模组。当物品运输线发生交叉时，它会自动选择合适的未配置交叉方案，避免玩家误放连接器后再拆除重建。

它主要服务于多种科技共存的地图；在没有歧义的场景中仍保持原版放置行为，因此不会干扰普通建造。

## 它会做什么

- 两条线路交叉时改放未配置反向分类器，两条线都继续工作，而不是互相覆盖。
- 覆盖传送带类（传送带、钛传送带、塑钢传送带、装甲传送带）与管道类（物品管道、装甲管道），混合交叉也算。
- 管道线撞上 3 格及以上的并排交叉阻挡带时，用物品管道桥整段跳过；1~2 格仍放分类器。
- 平行线、线路端点、非物品障碍、单科技星球保持原版行为。

## 设置

| 设置项 | 默认 | 作用 |
| --- | --- | --- |
| 传送带 交 传送带 | 开 | 传送带类横穿传送带类改放反向分类器 |
| 传送带 交 管道 | 开 | 传送带类横穿管道类 |
| 管道 交 传送带 | 开 | 管道类横穿传送带类 |
| 管道 交 管道 | 开 | 管道类横穿管道类 |
| 宽阻挡带改用管道桥 | 开 | 阻挡带超过 2 格时用物品管道桥跳过 |
| 只在任意星球生效 | 开 | 仅行星规则为“任意”（混合科技）时介入 |

## 安装

从 [Releases](https://github.com/DeterMination-Wind/SmartPlacement/releases) 下载通用 JAR，放入 Mindustry 的 mods 目录。该 JAR 同时支持桌面端和 Android。

需要 Mindustry 157 或更高版本，兼容原版 Mindustry 与 MindustryX。

## 构建

~~~powershell
.\gradlew.bat clean build
~~~

## Documentation / 文档

Developer docs (architecture, development, release, testing, glossary) live in [docs/README.md](docs/README.md)（架构、开发、发布、测试、术语表）。
