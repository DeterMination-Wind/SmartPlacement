# Smart Placement

> [!NOTE]
> 若有反馈，请发送反馈到bugsoc@qq.com / If you have any feedback, please send it to bugsoc@qq.com

> Make crossed item lines behave as expected, with less trial and error.

Smart Placement is a small Mindustry client-side helper for mixed-tech maps. When two item lines cross, it chooses the appropriate unconfigured crossing solution instead of leaving you to repair an accidental junction or bridge.

It is useful on maps where multiple technology sets share one construction space. Normal placement remains vanilla in situations that are not ambiguous, so the mod stays out of the way when no help is needed.

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

## 安装

从 [Releases](https://github.com/DeterMination-Wind/SmartPlacement/releases) 下载通用 JAR，放入 Mindustry 的 mods 目录。该 JAR 同时支持桌面端和 Android。

需要 Mindustry 157 或更高版本，兼容原版 Mindustry 与 MindustryX。

## 构建

~~~powershell
.\gradlew.bat clean build
~~~
