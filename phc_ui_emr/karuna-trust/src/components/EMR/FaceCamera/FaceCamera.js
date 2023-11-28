import React from "react";
import Webcam from "react-webcam";

class FaceCamera extends React.Component {
  render() {
    const videoConstraints = {
      facingMode: "user",
    };
    return (
      <div>
        <div>
          <Webcam videoConstraints={videoConstraints} />
        </div>
      </div>
    );
  }
}

export default FaceCamera;
