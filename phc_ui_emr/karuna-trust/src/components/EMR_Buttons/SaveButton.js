import React from "react";
import { Button } from "react-bootstrap";
import "../../css/RegForms.css";
import { Link } from "react-router-dom";

function SaveButton(props) {
  return (
    <div>
      {props.toLink ? (
        <Button
          as={Link}
          className={props.class_name}
          onClick={props.butttonClick}
          to={props.toLink}
        >
          {props.button_name}
        </Button>
      ) : (
        <Button
          className={props.class_name}
          onClick={props.butttonClick}
          disabled={props.btnDisable}
        >
          {props.button_name}
        </Button>
      )}
    </div>
  );
}

export default SaveButton;
