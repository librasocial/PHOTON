import React, { useEffect, useState } from "react";
import { over } from "stompjs";
import SockJS from "sockjs-client";
import axios from "axios";
import * as serviceHeaders from "../../../ConstUrl/serviceHeaders";

var stompClient = null;
const AbdmM2steps = () => {
  const [Authe, setAuthe] = useState([]);
  const [authMode, setauthMode] = useState("");
  const [transactionId, settransactionId] = useState("");
  const [Otp, setOtp] = useState("");
  const [HealthId, setHealthId] = useState();
  const [Name, setName] = useState("");
  const [gender, setgender] = useState("");
  const [yearOfBirth, setyearOfBirth] = useState();
  const [monthOfBirth, setmonthOfBirth] = useState("");
  const [dayOfBirth, setdayOfBirth] = useState("");
  const [district, setdistrict] = useState("");
  const [statee, setstatee] = useState("");
  const handleInput = (e) => {
    // console.log(e.target.value);
    setOtp(e.target.value);
  };
  const handleHealthId = (e) => {
    setHealthId(e.target.value);
  };
  useEffect(() => {
    // fetchAuth();
    return () => {};
  }, []);

  const API_BASE_URL =
    "https://dev-api-phcdt.sampoornaswaraj.org/abdm-svc/v0.5";

  const WS_BASE_URL =
    "https://dev-api-phcdt.sampoornaswaraj.org/abdm-svc/web-sockets";
  const API_TOKEN =
    "eyJraWQiOiJhK2dTaWRlRWoyczFqMTlsb3RPck9xMlNjVTJjTFRmbW5XSyt0NHVmVDdNPSIsImFsZyI6IlJTMjU2In0.eyJzdWIiOiI4MWI3ZWFiYy1hYzg3LTQxZmUtYjY3NS0zYjQ5YjM0NjM2YmMiLCJjb2duaXRvOmdyb3VwcyI6WyJzc2YtY29nbml0b2FkbWludXNlcnMiXSwiaXNzIjoiaHR0cHM6XC9cL2NvZ25pdG8taWRwLmFwLXNvdXRoLTEuYW1hem9uYXdzLmNvbVwvYXAtc291dGgtMV9paWNaRlhwOU8iLCJjbGllbnRfaWQiOiIzdjlzbWJqMXRkc2dvdWtmamY0bHRuajA3YyIsIm9yaWdpbl9qdGkiOiJmMWE5ZTc0Zi1jNjJkLTQ2MGEtODRjNS04MDJhYmFiYjdjOTQiLCJldmVudF9pZCI6IjY0Yjk4MDE3LTU4ZjgtNDY2ZS04YzEwLTI3NTgwYzZiMWJkNyIsInRva2VuX3VzZSI6ImFjY2VzcyIsInNjb3BlIjoiYXdzLmNvZ25pdG8uc2lnbmluLnVzZXIuYWRtaW4iLCJhdXRoX3RpbWUiOjE2OTE0Nzc3ODQsImV4cCI6MTY5MTU2NDE4NCwiaWF0IjoxNjkxNDc3Nzg0LCJqdGkiOiJhOTI3MWMxNi01MTA0LTQ5NTQtODg1Mi0xMzAzMjM0NTQ5MWMiLCJ1c2VybmFtZSI6InJha2VzaC1kZXYifQ.vg8jqKXiEofzFgTgvcEq2U1syNWNIPBxId15La5Ci0g9iUtGQ6TY15LPqlUty60HNAopnplBvIaUw9weZP9Nyi_C_isC8CzTZLoduvX1hIpeCnCzZ7_d_HVRsIPFyu5gBMq4kt7Nk33oZhVS2P-IOBXBlnfLjwi-L3ybaV-MPNDa9rxs-3aWHNNSP65lTrD_T0GwzyhFHtTZfkdF2YSpz4QjBZXoBhFKJ5yXb7uEjNAKZTIkq0dddUn2mSafaje1S9KJwsRAOWQxjABU-y92bj28EvJN4qiLrFwFJxSwPCl-GN1bb3q-k5ctFDuh69yiVrpl1DgFdBmZ_DGjo_-6Rw";

  const headers = {
    // mode: "no-cors",
    // "Access-Control-Allow-Origin": "*",
    Authorization: `Bearer ${API_TOKEN}`,
    "Content-Type": "application/json",
  };

  const connectToWebSocket = (uuid, onMessageReceived, onError) => {
    alert("hi");
    const Sock = new SockJS(WS_BASE_URL);
    stompClient = over(Sock);
    stompClient.connect(
      {
        mode: "no-cors",
        "Access-Control-Allow-Origin": "*",
      },
      // () => subscribeToTopic(uuid),
      () => subscribeToTopic(uuid),

      onError
    );
  };

  const subscribeToTopic = (uuid, onMessageReceived) => {
    stompClient.subscribe(`/topic/${uuid}`, async (payload) => {
      console.log(payload.body);
      const arr = [];
      const obj = await JSON.parse(payload.body);
      arr.push(obj);

      if (arr[0].data.auth.modes) {
        console.log(arr[0].data.auth.modes);
        setAuthe((prevAuthe) => [...prevAuthe, ...arr[0].data.auth.modes]);
      }
      // if (arr[0].data.auth.transactionId) {
      //   console.log(arr[0].data.auth.transactionId);
      //   settransactionId(arr[0].data.auth.transactionId);
      // }
      // if (arr[0].data.auth.patient) {
      //   setName(arr[0].data.auth.patient.name);
      //   setgender(arr[0].data.auth.patient.gender);
      //   setdistrict(arr[0].data.auth.patient.address.district);
      //   setstatee(arr[0].data.auth.patient.address.state);
      //   setyearOfBirth(arr[0].data.auth.patient.yearOfBirth);
      //   setmonthOfBirth(arr[0].data.auth.patient.monthOfBirth);
      //   setdayOfBirth(arr[0].data.auth.patient.dayOfBirth);
      //   console.log(arr[0].data.auth.patient.name);
      //   console.log(arr[0].data.auth.patient.gender);
      //   console.log(arr[0].data.auth.patient.address.line);
      //   console.log(arr[0].data.auth.patient.address.district);
      //   console.log(arr[0].data.auth.patient.address.state);
      // }
    });
  };

  const sendRequest = () => {
    // await fetchAuth();
    // alert("hi")
    fetchMode();
  };

  const fetchMode = () => {
    // try {
    const data = {
      query: {
        id: HealthId,
        purpose: "LINK",
        requester: {
          type: "HIP",
          id: "SSF_demo_123",
        },
      },
    };

    var requestOptions = {
      headers: serviceHeaders.myHeaders1,
      method: "POST",
      // mode: "cors",
      body: JSON.stringify(data),
    };

    console.log(requestOptions, "requestOptions");
    fetch(`${API_BASE_URL}/users/auth/fetch-modes`, requestOptions)
      .then((resp) => resp.json())
      .then((resp) => {
        // console.log(resp.data)
        connectToWebSocket(resp.id);
      })
      .catch((err) => {});
    // const resp = await axios.post(
    //   `${API_BASE_URL}/users/auth/fetch-modes`,
    //   data,
    //   {
    //     headers,
    //   }
    // );
    // console.log("Hererere :" + resp);
    // console.log(resp.data.id);
    // connectToWebSocket(resp.data.id);
    // } catch (error) {
    //   console.error("Error fetching modes:", error);
    // }
  };

  const otpVeri = async (item) => {
    try {
      const data = {
        requestId: "2cecfb1e-4d3d-4027-ab23-790132d1b1de",
        timestamp: "2023-08-02T07:47:45.659Z",
        query: {
          id: HealthId,
          purpose: "KYC_AND_LINK",
          authMode: authMode,
          requester: {
            type: "HIP",
            id: "SSF_demo_123",
          },
        },
      };

      var requestOptions = {
        headers: serviceHeaders.myHeaders1,
        method: "POST",
        // mode: "cors",
        body: JSON.stringify(data),
      };
      const response = await axios.post(
        `${API_BASE_URL}/users/auth/init`,
        requestOptions
      );

      console.log("Response:", response.data.id);
      connectToWebSocket(response.data.id);
    } catch (error) {
      console.error("Error:", error.message);
    }
  };

  const conAuth = async () => {
    var myHeaders = new Headers();
    myHeaders = headers;
    if (Otp && transactionId) {
      var raw = JSON.stringify({
        requestId: "11466ab9-ea13-4f5b-905b-c43825c3968c",
        timestamp: "2023-08-04T06:27:30.532Z",
        transactionId: `${transactionId}`,
        credential: {
          authCode: `${Otp}`,
        },
      });

      var requestOptions = {
        method: "POST",
        headers: serviceHeaders.myHeaders1,
        body: raw,
        redirect: "follow",
      };

      await fetch(`${API_BASE_URL}/users/auth/confirm`, requestOptions)
        .then((response) => response.text())
        .then((result) => {
          console.log(result);
          const data = JSON.parse(result);

          connectToWebSocket(data.id);
        })
        .catch((error) => console.log("error", error));
    }
  };

  return (
    <div>
      <div
        className="container"
        style={{
          padding: "7px",
          margin: "300px",
        }}
      >
        <input type="text" onChange={handleHealthId} />
        <button onClick={sendRequest}>Submit Health Id</button>
        {/* <p>{uuid}</p>
    
      <button onClick={otpVeri}>Mobile Otp</button>
    */}
        {Authe.map((item, i) => {
          return (
            <div style={{}}>
              <button
                onClick={() => {
                  setauthMode(item);
                  otpVeri();
                }}
                style={{ padding: "5px", margin: "5px" }}
                key={i}
              >
                {item}
              </button>
              <div></div>
            </div>
          );
        })}
        {/* */}
        <input type="text" onChange={handleInput} />
        <button onClick={conAuth}>Submit Otp</button>
      </div>
      {yearOfBirth && (
        <div>
          <h1>Name: {Name}</h1>
          <p>Gender: {gender}</p>
          <p>
            DOB: {yearOfBirth}-{monthOfBirth}-{dayOfBirth}
          </p>
          <p>
            Address: {district}, {statee}
          </p>
        </div>
      )}
    </div>
  );
};
export default AbdmM2steps;
