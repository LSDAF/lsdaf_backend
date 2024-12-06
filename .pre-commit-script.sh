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

# Run gdformat and mvn on each staged file
mvn_errors=()

GREEN='\033[0;32m'    # Green color
RED='\033[0;31m'      # Red color
YELLOW='\033[0;33m'   # Yellow color
NC='\033[0m'          # No Color
BOLD='\033[1m'        # Bold text

for file in $STAGED_FILES; do
    #echo -e "${GREEN}Staged file: ${BOLD}$file${NC}"
    echo -e $file
    if [[ $file == **/*.java ]]; then
        echo -e "${GREEN}Running mvn spotless:apply on ${BOLD}$file${NC}"
        mvn_output=$(mvn spotless:apply -DspotlessFiles=".$file" 2>&1)
        mvn_exit_code=$?
        echo $mvn_output
        echo $mvn_exit_code
        if [ $mvn_exit_code -ne 0 ]; then
          mvn_errors+=("$mvn_output")
          echo -e " - ${RED}${BOLD}mvn error${NC}:${YELLOW}${BOLD} $mvn_output${NC}"
        fi
    fi
done

if [ ${#mvn_errors[@]} -ne 0 ]; then
  echo -e "${BOLD}mvn errors found, aborting commit${NC}"
  exit 1
fi
# Optionally, you can add additional commands or checks here

# Add staged files to git
for file in $STAGED_FILES; do
    git add "$file"
done
