import React, { useState, useEffect } from "react";
import { Row, Col, Button, Form } from "react-bootstrap";
import Sidemenu from "../Sidemenus";
import ProGoogleMap from "./ProGoogleMap";
import AutoSuggest from "react-autosuggest";

export default function GisMap(props) {
  useEffect(() => {
    document.title = "EMR Geographic Information System";
  }, []);

  const [googlePage, setGooglePage] = useState(false);
  const gisState = () => {
    setGooglePage(true);
  };

  const companies = [
    { id: 1, name: "Suggnahalli" },
    { id: 2, name: "Chikkamaskal" },
    { id: 3, name: "Hulikal" },
    { id: 4, name: "Chikahhali" },
  ];

  const lowerCasedCompanies = companies.map((company) => {
    return {
      id: company.id,
      name: company.name.toLowerCase(),
    };
  });
  const [value, setValue] = useState("");
  const [suggestions, setSuggestions] = useState([]);

  function getSuggestions(value) {
    return lowerCasedCompanies.filter((company) =>
      company.name.includes(value.trim().toLowerCase())
    );
  }

  function clearMap() {
    setValue("");
  }

  return (
    <React.Fragment>
      <Row className="main-div ">
        <Col lg={2} className="side-bar">
          <Sidemenu />
        </Col>
        <Col lg={10} md={12} sm={12} xs={12}>
          {/* {googlePage == true ? */}
          <ProGoogleMap
            setGooglePage={setGooglePage}
            setValue={setValue}
            setSuggestions={setSuggestions}
          />
          {/* :
                        <div>
                            <div className='regHeader'>
                                <h1 className="register-Header">
                                    Geographic Information System
                                </h1>
                            </div>
                            <hr style={{ margin: "0px" }} />
                            <div className='pro-tab'>
                                <Row className='searchFilter-div google-select'>
                                    <Col md={6}>
                                        <Row className="">
                                            <Col md={8} className="search-select map-div">
                                                <div className="pro-select">
                                                    <Form.Group className="mb-3_drugname">
                                                        <Form.Label className="pro-label">PHC</Form.Label>
                                                        <AutoSuggest
                                                            suggestions={suggestions}
                                                            onSuggestionsClearRequested={() => setSuggestions([])}
                                                            onSuggestionsFetchRequested={({ value }) => {
                                                                setValue(value);
                                                                setSuggestions(getSuggestions(value));
                                                            }}
                                                            // onSuggestionSelected={(_, { suggestionValue }) =>

                                                            // }
                                                            getSuggestionValue={suggestion => suggestion.name}
                                                            renderSuggestion={suggestion =>
                                                                <div className="mapSuggest">
                                                                    <p className="subcenterLink" onClick={gisState}>{suggestion.name} Maddur Taluk Hospital Sub-divisional/Taluk Hospital</p>
                                                                    <p className="locationText">
                                                                        General Hospital Maddur, Old Mc Road Maddur, Maddur, Maddur Tq, Mandya Dist, Maddur, Mandya, Karnataka  571428
                                                                    </p>
                                                                    <p className="facilityHead">
                                                                        Facilities / Departments&nbsp;:
                                                                    </p>
                                                                    <p className="facilityText">
                                                                        Dentist, Lab, Pharmacy, Optometry
                                                                    </p>
                                                                    <p className="subcenterText">
                                                                        {suggestion.name}
                                                                    </p>
                                                                    <Button variant="secondary" id="google_visit" onClick={gisState}>GIS Mapping Information System</Button>
                                                                </div>
                                                            } type="search"
                                                            inputProps={{
                                                                placeholder: "Enter PHC Name",
                                                                value: value,
                                                                onChange: (_, { newValue, method }) => {
                                                                    setValue(newValue);
                                                                }
                                                            }}
                                                            highlightFirstSuggestion={true}
                                                        />
                                                    </Form.Group>
                                                    {value ?
                                                        <Button type="clear" onClick={clearMap} className="gmapClr">
                                                            <i className="bi bi-x"></i>
                                                        </Button>
                                                        : ""
                                                    }
                                                </div>
                                            </Col>
                                            <Col md={4}></Col>
                                        </Row>
                                    </Col>
                                    <Col md={6}></Col>
                                </Row>
                            </div>
                        </div>
                    } */}
        </Col>
      </Row>
    </React.Fragment>
  );
}
