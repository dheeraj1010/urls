#!/bin/bash

# Constants
APP_NAME="urls-0.0.1.jar"
LOG_FILE="logfile.log"
ERROR_LOG_FILE="error.log"
GITHUB_ENV_FILE="github_action_status.env" # File to communicate status to GitHub
CHECK_INTERVAL=5
MAX_CHECKS=5

# Logging functions
log_message() {
  echo "$(date +'%Y-%m-%d %H:%M:%S') - $1" | tee -a $LOG_FILE
}

log_error() {
  echo "$(date +'%Y-%m-%d %H:%M:%S') - ERROR: $1" | tee -a $ERROR_LOG_FILE
}

# Stop the running application
stop_app() {
  log_message "Attempting to stop the application..."

  # Get the process ID(s)
  PIDS=$(pgrep -f "$APP_NAME")
  if [ -n "$PIDS" ]; then
    log_message "Processes found with PIDs: $PIDS"

    # Send SIGTERM
    kill $PIDS
    sleep 5 # Allow for graceful shutdown

    # Force kill remaining processes, if any
    if pgrep -f "$APP_NAME" >/dev/null; then
      log_message "Processes still running. Sending SIGKILL..."
      kill -9 $PIDS
    else
      log_message "Application stopped successfully."
    fi
  else
    log_message "No running application found with name $APP_NAME."
  fi
}

# Start the application
start_app() {
  log_message "Starting application..."
  nohup java -Dspring.profiles.active=prod -jar "$APP_NAME" > "$LOG_FILE" 2>&1 &
  sleep 5 # Allow application to start
}

# Verify application status
verify_app() {
  log_message "Verifying application status..."
  CHECK_COUNT=0

  # Check if the application is running
  while [ $CHECK_COUNT -lt $MAX_CHECKS ]; do
    if pgrep -f "$APP_NAME" >/dev/null; then
      log_message "Application is running successfully!"
      return 0
    fi
    log_message "Waiting for application to be up... (retry $((CHECK_COUNT + 1))/$MAX_CHECKS)"
    sleep $CHECK_INTERVAL
    ((CHECK_COUNT++))
  done

  # If we reach here, the application failed to start
  log_error "Application failed to start after $MAX_CHECKS checks."
  return 1
}

# Deploy application
deploy_app() {
  log_message "Deployment started."

  # Stop existing application
  stop_app
  if [ $? -eq 0 ]; then
    log_message "Application stopped successfully."

    # Start new application
    start_app
    if [ $? -eq 0 ]; then
      log_message "Application started successfully."

      # Verify application state
      verify_app
      if [ $? -eq 0 ]; then
        log_message "Deployment completed successfully."
        echo "DEPLOYMENT_STATUS=success" > "$GITHUB_ENV_FILE" # Mark success for GitHub Workflow
        exit 0
      else
        log_error "Deployment failed during verification."
        echo "DEPLOYMENT_STATUS=failure" > "$GITHUB_ENV_FILE" # Mark failure for GitHub Workflow
        exit 1
      fi
    else
      log_error "Failed to start the application."
      echo "DEPLOYMENT_STATUS=failure" > "$GITHUB_ENV_FILE" # Mark failure for GitHub Workflow
      exit 1
    fi
  else
    log_error "Failed to stop the application."
    echo "DEPLOYMENT_STATUS=failure" > "$GITHUB_ENV_FILE" # Mark failure for GitHub Workflow
    exit 1
  fi
}

# Main execution
deploy_app
