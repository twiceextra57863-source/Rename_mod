#!/usr/bin/env bash
set -euo pipefail

REPO_DIR="$(cd "$(dirname "$0")/.." && pwd)"
cd "$REPO_DIR"

if ! git rev-parse --is-inside-work-tree >/dev/null 2>&1; then
  echo "[ERROR] Not a git repository: $REPO_DIR"
  exit 1
fi

CURRENT_BRANCH="$(git branch --show-current)"
if [[ -z "$CURRENT_BRANCH" ]]; then
  echo "[ERROR] No active branch found"
  exit 1
fi

if ! git remote get-url origin >/dev/null 2>&1; then
  echo "[ERROR] origin remote not configured."
  echo "Run: git remote add origin <your-github-repo-url>"
  exit 1
fi

REMOTE_URL="$(git remote get-url origin)"
echo "[INFO] origin: $REMOTE_URL"

echo "[INFO] Pushing branch '$CURRENT_BRANCH' to origin..."
git push -u origin "$CURRENT_BRANCH"

echo "[DONE] Push complete. Check your GitHub repo branch list/commits."
