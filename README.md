# LagKill Fabric Mod (1.21.4)

Ye repo ab sirf blueprint nahi, balki **working Fabric mod codebase scaffold + adaptive runtime logic** provide karta hai.

## Important Reality Note
`100% smooth gameplay` har phone/launcher/world scenario me guarantee nahi kiya ja sakta.
Is project ka goal hai:
- frame-time spikes kam karna,
- overload detect karke automatic profile switch karna,
- visual quality ko jitna possible ho maintain rakhte hue stutter reduce karna.


## VulkanMod jaisa banana hai? (Your request)
Maine VulkanMod repo study karke parity roadmap add kiya hai:
- `docs/VULKAN_PARITY_ROADMAP.md`

Isme clear plan hai kaise current adaptive mod ko step-by-step Vulkan-style smoothness ke paas le jana hai,
including mobile launcher constraints and practical benchmark targets.

## Vulkan-like setup (important)
Agar tum already `vulkanmod` use kar rahe ho, LagKill ab auto-detect karta hai aur Vulkan-oriented profiles apply kar sakta hai.
Config me `rendererPreference` key hai:
- `AUTO` (default): agar `vulkanmod` detected hai to vulkan profile use hogi
- `VULKAN_OPTIMIZED`: force vulkan tuning
- `DEFAULT`: normal tuning

> Note: `400 FPS` fixed guarantee har system pe possible nahi. Goal hai best possible smoothness + stable frame-time.

## Full mixin + accessor visual polish pack (new)
Tumhari request ke according mixin based visual-impact controls add kiye:
- Hurt screen shake disable (mixin)
- Fire overlay disable (mixin)
- MinecraftClient accessor hook ready
- Functional dashboard (F8 toggle) with category tabs + live buttons: General, Sodium, Iris, Camera, Water, Motion, Multiplayer

Visual tuning state class add:
- `LagKillVisualConfig` (motion blur strength, saturation boost, water clarity toggle, flame impact cleanup etc.)
- Dashboard me live toggle buttons: hurt shake, fire overlay, motion blur, water clarity, saturation +/- and smooth preset
- Persistent save file: `config/lagkill_visual.properties`

## C++ native acceleration + external tuning files (new)
Tumhari request ke hisaab se JNI bridge add kiya gaya hai:
- Java bridge: `src/main/java/com/rename_mod/lagkill/nativebridge/NativeBridge.java`
- Native code: `src/main/cpp/lagkill_native.cpp`
- CMake file: `src/main/cpp/CMakeLists.txt`

External tuning file bhi add hai:
- `native_profiles/gpu_profile_default.json`

Native library available hone par FPS cap suggestion native C++ path se aata hai.
Agar native library load nahi hoti to automatic Java fallback continue karta hai.

## Agar mod se lag badh raha ho (important fix)
Maine safety defaults improve kiye:
- `resourcePackWarmupEnabled=false` (startup spike avoid)
- `aggressiveSafeModeEnabled=true` (p95 high hone par auto competitive fallback)
- Startup controller object churn reduce kiya gaya for lower overhead.

> `0% lag` practical guarantee nahi hoti, lekin safe mode se severe frame spikes auto control me aayenge.

## Fast launch + faster ping/resource behavior (new)
Launch ko fast banane ke liye startup fast-path add hai:
- `startupFastPathEnabled=true`
- `startupFastPathTicks=600`

Resource pack / ping warmup options:
- `resourcePackWarmupEnabled=false` (default, spike avoid)
- `networkWarmupEnabled=true`
- `serverPingWarmupEnabled=true`

Ye features game start ke early phase me non-critical cost ko smooth karte hain aur network/resource readiness improve karte hain.

## No visual compromise mode (new)
Tumhari request ke hisaab se `visualIntegrityMode=true` default rakha gaya hai.
Is mode me mod visual knobs ko touch nahi karta:
- render distance unchanged
- entity distance scale unchanged

Lag reduction ke liye non-visual path use hota hai:
- adaptive FPS cap pacing
- simulation distance tuning
- multiplayer safety fallback

## Multiplayer 200+ FPS focus (new)
Tumhari request ke liye multiplayer path add kiya gaya hai:
- `multiplayerBoostEnabled=true` (default)
- `multiplayerTargetFps=200` (default)
- Server session detect hote hi `multiplayerVisual` / `multiplayerSafe` profile auto-switch hoti hai.

Ye visual quality aur stability ke balance ke liye tuned hai, especially PvP/multiplayer sessions me.

## Video Recording Focus (New)
Agar tum videos record karte ho, to mod me recording-oriented behavior add kiya gaya hai:
- `recordingModeEnabled=true` (default)
- `recordingFpsCap=60` (default)
- Stable recording ke liye `recordingQuality` and heavy load pe `recordingSafe` profile auto-switch hoti hai.

Config file: `config/lagkill.properties`

## Implemented
- Fabric mod setup (Gradle + Loom)
- Config persistence (`config/lagkill.properties`)
- Frame-time monitor (smoothed + p95)
- Adaptive overload tracker
- Runtime profile applier (view distance / simulation distance / FPS cap / entity distance scale)
- GitHub Actions build workflow + artifact upload

## Profiles
- `QUALITY`: higher visuals
- `BALANCED`: daily play default
- `COMPETITIVE`: low-latency bias
- `RECORDING_QUALITY` (auto): videos ke liye visual + stable cap
- `RECORDING_SAFE` (auto): record mode me overload fallback
- `EMERGENCY` (auto): heavy lag ke waqt aggressive fallback

## Build
> Java 21 required

```bash
gradle build --no-daemon
```


## Agar repo me change visible nahi ho rahe
Aksar reason hota hai ki latest commits pull nahi hue.

```bash
git fetch --all
git log --oneline -n 5
git pull
```

## PR nahi ho raha? (Fix)
Agar changes hone ke baad pull request create nahi ho raha, ye steps follow karo:

```bash
git remote -v
git branch --show-current
./scripts/create_pr.sh main "LagKill update"
```

Agar `gh` CLI installed/authenticated nahi hai to script push karke manual compare URL dega.

## Repo pe files kyu nahi dikh rahi?
Agar yaha changes huye hain lekin tumhare GitHub repo me nahi dikh rahe, to most common reason:
- `origin` remote set nahi hai, ya
- branch push nahi hui.

Check karo:

```bash
git remote -v
git branch --show-current
git log --oneline -n 5
```

Push helper:

```bash
./scripts/push_to_github.sh
```

Agar `origin` missing ho to pehle:

```bash
git remote add origin <your-github-repo-url>
./scripts/push_to_github.sh
```

## JAR download kaise karoge?
### Option 1: GitHub Actions artifact (recommended)
1. Repo me **Actions** tab open karo.
2. Latest successful `build` workflow run open karo.
3. Neeche **Artifacts** section me `lagkill-mod-jar` download karo.
4. Zip extract karo, jar `build/libs/` ke andar milega.

### Option 2: Local build
`build/libs/` folder me jar generate hota hai after successful build.

## CI
`.github/workflows/build.yml` push / pull_request / manual dispatch pe build run karta hai aur jar artifact upload karta hai.

## Next upgrades
- Particle budget limiter hook
- Chunk upload pacing integration
- PvP-mode quick toggle keybind
- Optional JNI bridge (C++) with safe fallback
