import React from "react";
import { useEffect } from "react";
import { Button, Col, Form, Row, Image } from "react-bootstrap";
import { useSelector } from "react-redux";
import { saveAs } from "file-saver";

function AdharProfile(props) {
  const imageRef = React.useRef();

  const { profileData } = useSelector((state) => state.abhaData);
  const { qrData } = useSelector((state) => state.abhaData);
  const { cardData } = useSelector((state) => state.abhaData);
  const { svgData } = useSelector((state) => state.abhaData);
  const { pngData } = useSelector((state) => state.abhaData);

  useEffect(() => {
    if (pngData) {
      imageRef.current.src = pngData;
    }
  }, [pngData]);

  // const downLoadImag = (pngData) => {
  //     let a = document.createElement('a');
  //     // a.download = pngData.replace(/^.*[\\\/]/, '');
  //     a.href = pngData;
  //     document.body.appendChild(a);
  //     a.click();
  //     a.remove();
  // }

  const downLoadImag = () => {
    saveAs(pngData, "image.png"); // Put your image url here.
  };

  const backToHomePage = () => {
    props.AbhaModalClose();
  };

  return (
    <div>
      <div className="abha-profile-div" align="center">
        <h4>Profile Details (as per Aadhaar)</h4>
      </div>
      <div align="center">
        <Image ref={imageRef} id="myImg" width="500" height="250" rounded />
      </div>
      <div>
        <Form className="adhar-otp-div">
          <div className="modal-btn-div">
            <div className="">
              <Button
                variant="primary"
                className="genIdBtn"
                value="mobile"
                onClick={backToHomePage}
              >
                Back To Home
              </Button>
              <Button
                variant="primary"
                className="genIdBtn"
                value="mobile"
                onClick={(e) => downLoadImag(pngData)}
              >
                Download ABHA Number Card
              </Button>
            </div>
          </div>
        </Form>
      </div>
    </div>
  );
}

export default AdharProfile;
