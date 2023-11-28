import React, { useState, useEffect } from "react";
import * as constant from "../ConstUrl/constant";
import * as serviceHeaders from "../ConstUrl/serviceHeaders";

function SignedHealthIDImage(props) {
  let healthID = props.healthID;
  const [image, setImage] = useState("../img/admin.png");

  useEffect(() => {
    if (healthID != undefined && healthID != "") {
      fetch(
        `${constant.ApiUrl}/healthIds/imageUrl?healthId=${healthID}`,
        serviceHeaders.getRequestOptions
      )
        .then((res) => res.json())
        .then((res) => {
          if (res.preFetchURL) {
            setImage(res["preFetchURL"]);
          } else {
            setImage("../img/admin.png");
          }
        })
        .catch((err) => {});
    } else {
      setImage("../img/admin.png");
    }
  }, [healthID]);

  // const getSignedImageUrl = async (healthID) => {
  //     var myHeaders = new Headers();
  //     let authtoken = sessionStorage.getItem("token");
  //     myHeaders.append("Authorization", `Bearer ${authtoken}`);
  //     myHeaders.append("Content-Type", "application/json");
  //     myHeaders.append("Accept", "application/json");
  //     var requestOptions = {
  //         method: "GET",
  //         headers: myHeaders,
  //         redirect: "follow",
  //     };
  //     fetch(`${constant.ApiUrl}/healthIds/imageUrl?healthId=${healthID}`, requestOptions)
  //         .then((res) => res.json())
  //         .then((res) => {

  //         })

  //     return (await (await fetch(
  //         `${constant.ApiUrl}/healthIds/imageUrl?healthId=${healthID}`,
  //         requestOptions
  //     )).json())["preFetchURL"];
  // };
  return (
    <>
      <img
        onError={() => {
          setImage("../img/admin.png");
        }}
        src={image}
        className="dataImg rounded_pat"
        alt="person-image"
      />
    </>
  );
}

export default SignedHealthIDImage;
