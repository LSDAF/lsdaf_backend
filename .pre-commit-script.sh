#!/bin/bash

echo "YEEEEEES"


# Create a virtual environment
python3 -m venv .venv

# Activate the virtual environment
source .venv/bin/activate

# Install the required packages
pip install --quiet -r requirements.txt

# Get a list of staged files
STAGED_FILES=$(git diff --cached --name-only --diff-filter=ACM)

# Run gdformat and gdlint on each staged file
gdlint_errors=()

GREEN='\033[0;32m'    # Green color
RED='\033[0;31m'      # Red color
YELLOW='\033[0;33m'   # Yellow color
NC='\033[0m'          # No Color
BOLD='\033[1m'        # Bold text

for file in $STAGED_FILES; do
    echo -e "${GREEN}Staged file: ${BOLD}$file${NC}"

    if [[ $file == *.gd ]]; then
        if [[ $file == addons/* ]]; then
          echo -e "${YELLOW}Skipping ${BOLD}$file${NC}"
            continue
        fi
        echo -e "${GREEN}Running gdformat on ${BOLD}$file${NC}"
        gdformat "$file"
        echo -e "${GREEN}Running gdlint on ${BOLD}$file${NC}"
        gdlint_output=$(gdlint "$file" 2>&1)
        gdlint_exit_code=$?
        if [ $gdlint_exit_code -ne 0 ]; then
          gdlint_errors+=("$gdlint_output")
          echo -e " - ${RED}${BOLD}Gdlint error${NC}:${YELLOW}${BOLD} $gdlint_output${NC}"
        fi
    fi
done

if [ ${#gdlint_errors[@]} -ne 0 ]; then
  echo -e "${BOLD}Gdlint errors found, aborting commit${NC}"
  exit 1
fi
# Optionally, you can add additional commands or checks here

# Add staged files to git
for file in $STAGED_FILES; do
    git add "$file"
done
