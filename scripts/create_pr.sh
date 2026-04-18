#!/usr/bin/env bash
set -euo pipefail

BASE_BRANCH="${1:-main}"
PR_TITLE="${2:-LagKill update}"
PR_BODY_FILE="${3:-}"

REPO_DIR="$(cd "$(dirname "$0")/.." && pwd)"
cd "$REPO_DIR"

CURRENT_BRANCH="$(git branch --show-current)"
if [[ -z "$CURRENT_BRANCH" ]]; then
  echo "[ERROR] No active branch found"
  exit 1
fi

if ! git remote get-url origin >/dev/null 2>&1; then
  echo "[ERROR] origin remote not configured"
  echo "Run: git remote add origin <your-repo-url>"
  exit 1
fi

echo "[INFO] Pushing '$CURRENT_BRANCH' to origin..."
git push -u origin "$CURRENT_BRANCH"

if ! command -v gh >/dev/null 2>&1; then
  echo "[WARN] GitHub CLI (gh) not installed."
  echo "Create PR manually: https://github.com/<owner>/<repo>/compare/${BASE_BRANCH}...${CURRENT_BRANCH}?expand=1"
  exit 0
fi

if ! gh auth status >/dev/null 2>&1; then
  echo "[ERROR] gh not authenticated. Run: gh auth login"
  exit 1
fi

if [[ -n "$PR_BODY_FILE" ]]; then
  gh pr create --base "$BASE_BRANCH" --head "$CURRENT_BRANCH" --title "$PR_TITLE" --body-file "$PR_BODY_FILE"
else
  gh pr create --base "$BASE_BRANCH" --head "$CURRENT_BRANCH" --title "$PR_TITLE" --body "Automated PR from scripts/create_pr.sh"
fi

echo "[DONE] PR created successfully."
