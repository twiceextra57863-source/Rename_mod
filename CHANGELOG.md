# Changelog

## 0.9.2 - Anti-regression lag safety tuning
- Added `aggressiveSafeModeEnabled` config for automatic fallback when p95 frametime is very high.
- Reduced runtime overhead by removing per-tick `StartupFastPathController` recreation.
- Changed default `resourcePackWarmupEnabled` to `false` to avoid startup spikes.
- Added runtime logging refinements and README guidance for high-lag scenarios.

## 0.9.1 - PR creation helper script
- Added `scripts/create_pr.sh` to automate push + PR creation via GitHub CLI.
- Added README troubleshooting section for PR creation failures.

## 0.9.0 - Persistent visual config + category dashboard tabs
- Added persistent visual settings file (`config/lagkill_visual.properties`).
- `LagKillVisualConfig` now supports bootstrap/load/save persistence.
- Dashboard now has category tabs and category-specific control panels.

## 0.8.1 - Functional dashboard interactions
- Dashboard (F8) now toggles open/close and supports live visual setting buttons.
- Added live config mutation methods in `LagKillVisualConfig`.
- Dashboard controls now update labels and allow preset/saturation adjustments.

## 0.8.0 - Mixin/accessor visual controls + dashboard skeleton
- Added client mixins for hurt shake suppression and fire overlay suppression.
- Added `MinecraftClientAccessor` example accessor.
- Added `LagKillVisualConfig` for visual-impact toggles and polish settings.
- Added `LagKillDashboardScreen` (F8) with category list (General, Sodium, Iris, Camera, Water, Motion, Multiplayer).
- Added `lagkill.mixins.json` and wired it in `fabric.mod.json`.

## 0.7.0 - JNI native FPS path + external GPU profile files
- Added JNI bridge (`NativeBridge`) and native C++ implementation (`lagkill_native.cpp`) for FPS cap suggestion.
- Added `src/main/cpp/CMakeLists.txt` for native library build setup.
- Added external tuning profile `native_profiles/gpu_profile_default.json`.
- Integrated native path into `NonVisualLagController` with safe Java fallback.

## 0.6.0 - Startup fast-path and warmup services
- Added `StartupFastPathController` for faster early launch behavior.
- Added network DNS warmup (`NetworkWarmupService`) for quicker server readiness/ping path.
- Added `ResourcePackWarmupService` for resource-pack manager warmup.
- Added new config toggles: `startupFastPathEnabled`, `startupFastPathTicks`, `resourcePackWarmupEnabled`, `networkWarmupEnabled`, `serverPingWarmupEnabled`.

## 0.5.0 - No visual compromise lag fixes
- Added `visualIntegrityMode` to keep render visuals untouched by default.
- Added `NonVisualLagController` for adaptive FPS cap pacing based on p95 frame-time.
- Added config flags for non-visual mitigation flow (`networkBurstGuardEnabled`, `backgroundTaskThrottleEnabled`).

## 0.4.0 - Multiplayer boost profiles
- Added multiplayer session detection (`MultiplayerAdvisor`).
- Added multiplayer-focused profiles targeting 200+ FPS where hardware allows.
- Added config keys `multiplayerBoostEnabled` and `multiplayerTargetFps`.

## 0.3.1 - Vulkan parity roadmap docs
- Added `docs/VULKAN_PARITY_ROADMAP.md` with phased plan inspired by VulkanMod architecture.
- Added README section linking Vulkan parity plan and benchmark targets.

## 0.3.0 - Vulkan-aware profile tuning
- Added backend detection for `vulkanmod`.
- Added `rendererPreference` config (`AUTO`, `DEFAULT`, `VULKAN_OPTIMIZED`).
- Added Vulkan-oriented runtime profiles for better visual + FPS balance.

## 0.2.0 - Recording + Artifact flow
- Recording-focused runtime profiles add kiye (`recordingQuality`, `recordingSafe`).
- `recordingModeEnabled` and `recordingFpsCap` config options add kiye.
- CI workflow me JAR artifact upload (`lagkill-mod-jar`) add kiya.

## 0.1.0 - Initial scaffold
- Fabric 1.21.4 mod scaffold setup.
- Adaptive monitoring core (`FrameTimeMonitor`, `AdaptiveQualityController`).
- Basic runtime tuner + persistent config.
