import React, { useState, useRef } from "react";
import IdleTimer from "react-idle-timer";
import SessionTimeoutDialog from "./SessionTimeoutDialog";

let countdownInterval;
let timeout = 1000 * 60 * 30;

const SessionTimeout = () => {
  const idleTimer = useRef(null);

  const [timeoutModalOpen, setTimeoutModalOpen] = useState(false);
  const [timeoutCountdown, setTimeoutCountdown] = useState(0);

  const onActive = () => {
    // timer reset automatically.
    if (!timeoutModalOpen) {
      clearSessionInterval();
      clearSessionTimeout();
    }
  };

  const onIdle = () => {
    const delay = 1000 * 5;
    if (sessionStorage.getItem("login") && !timeoutModalOpen) {
      setTimeout(() => {
        let countDown = 60;
        setTimeoutModalOpen(true);
        setTimeoutCountdown(countDown);
        countdownInterval = setInterval(() => {
          if (countDown > 0) {
            setTimeoutCountdown(--countDown);
          } else {
            handleLogout();
          }
        }, 1000);
      }, delay);
    }
  };

  const clearSessionTimeout = () => {
    clearTimeout(timeout);
  };

  const clearSessionInterval = () => {
    clearInterval(countdownInterval);
  };

  const handleLogout = () => {
    setTimeoutModalOpen(false);
    clearSessionInterval();
    clearSessionTimeout();
    sessionStorage.clear();
    window.location.href = "/";
  };

  const handleContinue = () => {
    setTimeoutModalOpen(false);
    clearSessionInterval();
    clearSessionTimeout();
  };

  return (
    <>
      <IdleTimer
        ref={idleTimer}
        onActive={onActive}
        onIdle={onIdle}
        debounce={250}
        timeout={timeout}
      />
      <SessionTimeoutDialog
        countdown={timeoutCountdown}
        onContinue={handleContinue}
        onLogout={handleLogout}
        open={timeoutModalOpen}
      />
    </>
  );
};

export default SessionTimeout;
