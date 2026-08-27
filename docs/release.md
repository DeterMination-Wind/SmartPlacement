# 版本与发布

Smart Placement 的版本与发布纪律。本仓库无 CI：版本、构建、上传全部本地完成。

## 版本号体系

- 语义化版本字符串（如 `1.0.0`），需同步改两处：`mod.json` 的 `version` 与 `build.gradle` 的 `version`。
- git tag 用 `v*` 形式（如 `v1.0.0`）；每个版本配一份 `release_notes_v<版本>.md`（中英双语）。

## 发布前本地必做

1. 更新 `mod.json` + `build.gradle` 的版本号，写 `release_notes_v<版本>.md`。
2. `.\gradlew.bat clean build`，确认 `verifyRules` 通过。
3. 核实产物（`build` 会自动暂存到 `../构建/SmartPlacement/` 并重命名）：
   - `SmartPlacement-v<版本>.jar`（dist/SmartPlacement.jar 的重命名副本）
   - `SmartPlacement-v<版本>.zip`（dist/SmartPlacement.zip 的重命名副本）
4. [手测清单](testing.md)全过后再打 tag、创建 GitHub Release 并上传两个文件。

## 构建产物链

```text
classes ─► d8InputJar ─► dexAndroid ─► deploy ─► releaseZip ─► stageRelease
           (仅 class)     (d8 --min-api 14    (dist/SmartPlacement.jar  (dist/SmartPlacement.zip  (重命名复制到
                           --release，classpath  classes+classes.dex      jar+两份 README)        ../构建/SmartPlacement/)
                           含 Mindustry/Arc/    +mod.json)
                           android.jar)
```

- `dist/` 在 `.gitignore` 中，是纯本地产物；`clean` 会连 dist/ 与暂存目录一起删除。
- 可分发形态只有 `deploy` / `releaseZip` 的产物；`d8InputJar` 的中间 jar 不允许外发。

## Release 资产安全规则

Mindustry 游戏内安装器取 Release API 返回的第一个 `.jar`，且不按操作系统挑资产：

- 一个 Release 有且只有一个 `.jar` 资产，即 `SmartPlacement-v<版本>.jar`（含 `mod.json` + 主类 + `classes.dex`）。
- 严禁上传 d8 输入 jar 等中间产物；发布后用 `gh release view <tag> --json assets` 复查，发现多余 `.jar` 立即删除。
