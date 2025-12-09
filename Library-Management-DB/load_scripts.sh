#!/bin/bash
set -e

echo "🚀 Starting Custom SQL Execution..."

# 1. Execute all SQL files in the 'create' folder
# CHANGED: Path is now /sql_scripts/create
if [ -d "/sql_scripts/create" ]; then
    echo "📂 Processing Create Scripts..."
    for f in "/sql_scripts/create"/*.sql; do
        if [ -f "$f" ]; then
            echo "   - Running $f"
            mysql -u root -p"$MYSQL_ROOT_PASSWORD" "$MYSQL_DATABASE" < "$f"
        fi
    done
fi

# 2. Execute all SQL files in the 'insert' folder
# CHANGED: Path is now /sql_scripts/insert
if [ -d "/sql_scripts/insert" ]; then
    echo "📂 Processing Insert Scripts..."
    for f in "/sql_scripts/insert"/*.sql; do
        if [ -f "$f" ]; then
            echo "   - Running $f"
            mysql -u root -p"$MYSQL_ROOT_PASSWORD" "$MYSQL_DATABASE" < "$f"
        fi
    done
fi

echo "✅ All scripts executed successfully!"
