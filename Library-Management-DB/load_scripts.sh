#!/bin/bash

# Exit immediately if a command exits with a non-zero status
set -e

echo "🚀 Starting Custom SQL Execution..."

# 1. Execute all SQL files in the 'create' folder
if [ -d "/sql_scripts/1. create" ]; then
    echo "📂 Processing Create Scripts..."
    for f in "/sql_scripts/1. create"/*.sql; do
        if [ -f "$f" ]; then
            echo "   - Running $f"
            # Execute SQL file using the MySQL client inside the container
            mysql -u root -p"$MYSQL_ROOT_PASSWORD" "$MYSQL_DATABASE" < "$f"
        fi
    done
fi

# 2. Execute all SQL files in the 'insert' folder
if [ -d "/sql_scripts/2. insert" ]; then
    echo "📂 Processing Insert Scripts..."
    for f in "/sql_scripts/2. insert"/*.sql; do
        if [ -f "$f" ]; then
            echo "   - Running $f"
            mysql -u root -p"$MYSQL_ROOT_PASSWORD" "$MYSQL_DATABASE" < "$f"
        fi
    done
fi

echo "✅ All scripts executed successfully!"
