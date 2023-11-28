import React, { useState, useEffect } from "react";
// import "./styles.css";

const SpeechRecognition =
  window.SpeechRecognition || window.webkitSpeechRecognition;
const mic = new SpeechRecognition();

mic.continuous = true;
mic.interimResults = true;
mic.lang = "en-US";

function SpeechRecgongition(props) {
  const [isListening, setIsListening] = useState(false);
  const [note, setNote] = useState(null);
  const [savedNotes, setSavedNotes] = useState([]);

  useEffect(() => {
    handleListen();
  }, [isListening]);

  const handleListen = () => {
    if (isListening) {
      mic.start();
      mic.onend = () => {
        mic.start();
      };
    } else {
      mic.stop();
      mic.onend = () => {};
    }
    mic.onstart = () => {};

    mic.onresult = (event) => {
      const transcript = Array.from(event.results)
        .map((result) => result[0])
        .map((result) => result.transcript)
        .join("");
      // setNote(transcript);
      props.setComplaint(transcript);
      mic.onerror = (event) => {};
    };
  };

  const handleSaveNote = () => {
    setSavedNotes([...savedNotes, note]);
    setNote("");
  };

  return (
    <>
      {/* <h1>Voice Notes</h1> */}
      <div className="container">
        <div className="mic-box">
          {/* <h2>Current Note</h2> */}
          {/* {isListening ? <span>🎙️</span> : <span>🛑🎙️</span>} */}
          {/* <button onClick={handleSaveNote} disabled={!note}>
            Save Note
          </button> */}
          <button
            className="mic-btn"
            onClick={() => setIsListening((prevState) => !prevState)}
          >
            {isListening ? (
              <i className="bi bi-mic"></i>
            ) : (
              <i className="bi bi-mic-mute"></i>
            )}
            <br />
            {/* {isListening ? "Speack here" : ""} */}
          </button>
          {/* <p>{note}</p> */}
        </div>
        {/* <div className="box">
          <h2>Notes</h2>
          {savedNotes.map((n) => (
            <p key={n}>{n}</p>
          ))}
        </div> */}
      </div>
    </>
  );
}

export default SpeechRecgongition;
