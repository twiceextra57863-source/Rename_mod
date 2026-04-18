# LagKill Fabric Mod (1.21.4)

Ye repo ab sirf blueprint nahi, balki **working Fabric mod codebase scaffold + adaptive runtime logic** provide karta hai.

## Important Reality Note
`100% smooth gameplay` har phone/launcher/world scenario me guarantee nahi kiya ja sakta.
Is project ka goal hai:
- frame-time spikes kam karna,
- overload detect karke automatic profile switch karna,
- visual quality ko jitna possible ho maintain rakhte hue stutter reduce karna.

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
