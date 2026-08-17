# 智能放置

[中文](README_zh.md) | [English](README.md)

智能放置是一个小型 Mindustry Java 模组，用于改进物品运输线的拖拽交叉行为。在双科技地图中，互相垂直的物品运输线会自动使用空配置反向分类器，不再生成交叉器或桥。

## 行为

当前规则的行星为 `Planets.sun` 时启用双科技放置。规则界面中的“任意”行星选项内部同样使用这个值。

支持以下垂直交叉，并包含装甲传送带、装甲管道等子类：

| 新拖拽的运输线 | 已有运输线 | 结果 |
|---|---|---|
| 传送带 | 传送带 | 空配置反向分类器 |
| 传送带 | 物品管道 | 空配置反向分类器 |
| 物品管道 | 传送带 | 空配置反向分类器 |
| 物品管道 | 物品管道 | 空配置反向分类器 |
| 塑钢传送带 | 任意受支持物品运输线 | 空配置反向分类器 |

在所有单科技行星中，智能放置完全保留原版铺线行为，包括原版交叉器和桥的自动生成。平行运输线、拖拽端点和非物品运输障碍也继续使用原版逻辑。

## 安装

从 [Releases](https://github.com/DeterMination-Wind/SmartPlacement/releases) 下载通用 JAR，放入 Mindustry 的 `mods/` 目录。该 JAR 同时包含桌面字节码与 Android `classes.dex`。

需要 Mindustry 157 或更高版本，兼容原版 Mindustry 与 MindustryX。

## 构建

```powershell
gradlew clean build
```

输出：

- `dist/SmartPlacement.jar` - 可直接安装的通用模组
- `dist/SmartPlacement.zip` - 通用 JAR 与中英文文档
- `../构建/SmartPlacement/SmartPlacement-v1.0.0.jar`
- `../构建/SmartPlacement/SmartPlacement-v1.0.0.zip`

Android dex 步骤需要 D8 与 `android.jar`。工作区工具链未提供它们时，请设置 `D8_PATH`、`ANDROID_SDK_ROOT` 或 `ANDROID_HOME`。
