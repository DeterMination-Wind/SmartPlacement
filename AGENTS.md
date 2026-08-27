# SmartPlacement 开发指南（AGENTS.md）

本文件面向在本仓库工作的 AI 代理与人类协作者。面向人类的分类文档在 [`docs/`](docs/README.md)，两者冲突时以本文件为准。

## 项目定位

- Smart Placement（智能放置）是 Mindustry 纯客户端辅助模组：在混合科技地图（规则行星为 `sun`/"任意"）画运输线时，垂直交叉自动改放空配置反向分类器；其余场景完全保持原版。
- 全部逻辑在 `src/main/java/smartplacement/SmartPlacementMod.java` 一个文件里；无设置项、无 bundle 文案、不新增可放置方块。
- 本仓库同时是 Neon 聚合模组的子模组真源（Neon `tools/submods.json` 中 id=`smp`，`localPath` 指向本目录）。同步方向永远是：本仓库 → Neon。当前源码不含 `bekBundled`，聚合钩子由 Neon 同步脚本注入；不要在 Neon 侧手改 `smartplacement` 包。

## 文件结构

- `src/main/java/smartplacement/`：入口 `SmartPlacementMod`（`init()` + Trigger.update 交换 proxy）与内部类 `PlacementProxy`。
- `src/test/java/smartplacement/`：`SmartPlacementRulesTest`（main() 断言）。
- `build.gradle`：任务链与产物复制；`mod.json`：描述文件（`main`、`minGameVersion 157`、`hidden`）。
- `dist/`：本地产物（gitignore）；发布暂存目录是工作区的 `构建/SmartPlacement/`。

## 构建与验证

```powershell
.\gradlew.bat clean build     # 编译 + verifyRules + dist/ 产物 + 暂存到 ../构建/SmartPlacement/
.\gradlew.bat verifyRules     # 只跑规则回归（已挂接 check）
```

- 编译依赖工作区本地 `../Mindustry-master`、`../Arc` 的 classes 目录，无 JitPack 回退。
- Java 8 字节码目标；D8 探测顺序：`D8_PATH` → Android SDK `build-tools` → 工作区 `commandlinetools-win-*`。
- 保持标准 `test` 任务的禁用与 `verifyRules` 对 `check` 的挂接，不得拆链。

## 代码约束

- 判定逻辑（`crossingReplacement` / `replacementEnabled`）保持 static 纯函数，改动必须同步补 `SmartPlacementRulesTest` 断言。
- 无歧义场景（平行线、线端点、单科技、非运输线）必须返回原版回退，不允许扩大替换范围。
- `PlacementProxy` 必须保持 `content.remove()` 排除与画线结束后还原 `input.block`，不得让替身外露到放置 UI。

## 版本与提交

- 版本号同时改 `mod.json` 与 `build.gradle`，tag 用 `v*`，每个版本配 `release_notes_v<版本>.md`。
- 提交信息以 `SmartPlacement:` 开头（如 `SmartPlacement: release v1.0.0`）；纯文档改动可用 `docs:` 前缀。
- Release 资产安全：一个 Release 只能有唯一可安装 `.jar`（含 `classes.dex`），发布后用 `gh release view <tag> --json assets` 复查，发现多余 `.jar` 立即删除。

## 文档索引

[`docs/README.md`](docs/README.md)：架构 / 开发 / 发布 / 测试 / 术语表导航。
