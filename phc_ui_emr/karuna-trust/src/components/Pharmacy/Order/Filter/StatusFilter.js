import React, { useState } from "react";
import { observer } from "mobx-react-lite";
import { Button, Dropdown, Form, Row, Col } from "react-bootstrap";
import "./styles.css";

const CheckboxMenu = React.forwardRef(
  (
    {
      children,
      style,
      className,
      "aria-labelledby": labeledBy,
      onSelectAll,
      onSelectNone,
    },
    ref
  ) => {
    return (
      <div
        ref={ref}
        style={style}
        className={`${className} CheckboxMenu`}
        aria-labelledby={labeledBy}
      >
        <div
          className="d-flex flex-column"
          style={{ maxHeight: "calc(300px)" }}
        >
          <ul
            className="list-unstyled flex-shrink mb-0"
            style={{ overflow: "auto" }}
          >
            {children}
          </ul>
          {/* <Row className="border-top pt-2 pb-0">
                        <Col md={6}>
                            <Button className="filterbtn btn btn-link" onClick={onSelectAll}>
                                Select All
                            </Button>
                        </Col>
                        <Col md={6} align="right">
                            <Button className="filterbtn btn btn-link" onClick={onSelectNone}>
                                Reset
                            </Button>
                        </Col>
                    </Row> */}
        </div>
      </div>
    );
  }
);

const CheckDropdownItem = React.forwardRef(
  ({ children, id, checked, onChange }, ref) => {
    return (
      <Form.Group ref={ref} className="dropdown-item mb-0" controlId={id}>
        <Form.Check
          type="checkbox"
          label={children}
          checked={checked}
          onChange={onChange}
        />
      </Form.Group>
    );
  }
);

const StatusFilter = observer((props) => {
  const [checked, setChecked] = useState([]);
  let items2 = props.items2;
  let isChecked = (item) =>
    checked.includes(item) ? "checked-item" : "not-checked-item";

  const handleChecked = (e, label) => {
    if (e.target.checked == true) {
      items2.find((i) => i.label === label).checked = e.target.checked;
      props.setStatusValue([...props.statusValue, label]);
    } else {
      items2.find((i) => i.label === label).checked = e.target.checked;
      props.setStatusValue(props.statusValue.filter((id) => id !== label));
    }
  };

  const handleSelectAll = () => {
    items2.map((i) => {
      props.statusValue.push(i.label);
    });
    items2.forEach((i) => {
      i.checked = true;
    });
  };

  const handleSelectNone = () => {
    items2.forEach((i) => {
      i.checked = false;
      props.setStatusValue([]);
    });
  };

  return (
    <Dropdown>
      <Dropdown.Toggle
        variant="primary"
        id="dropdown-basic"
        className="btn btn-primary dotEnBtn dotFilterBtn"
      >
        <i className="fa fa-filter inwards-filter" aria-hidden="true"></i>
      </Dropdown.Toggle>

      <Dropdown.Menu
        as={CheckboxMenu}
        onSelectAll={handleSelectAll}
        onSelectNone={handleSelectNone}
      >
        {items2.map((i) => (
          <React.Fragment key={i.id}>
            <Dropdown.Item
              className={isChecked(i.label)}
              as={CheckDropdownItem}
              value={i.label || ""}
              id={i.id}
              checked={i.checked}
              onChange={(e) => handleChecked(e, i.label)}
            >
              <p id="content">{i.label}</p>
            </Dropdown.Item>
          </React.Fragment>
        ))}
      </Dropdown.Menu>
    </Dropdown>
  );
});

export default StatusFilter;
