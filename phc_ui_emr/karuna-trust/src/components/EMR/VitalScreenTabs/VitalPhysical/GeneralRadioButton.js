import React from "react";

function GeneralRadioButton(props) {
  let phyExamItems = props.phyExamItems;
  return (
    <div className="new-assessment-body">
      {phyExamItems.map((formItem, i) => (
        <React.Fragment key={i}>
          {props.assessName == "Conscious" &&
            formItem.groupName == "Conscious" && (
              <>
                {formItem.elements.map((drpItem, drpIndex) => (
                  <React.Fragment key={drpIndex}>
                    <input
                      className="checkbox-tools"
                      type="checkbox"
                      name="Conscious"
                      id={drpItem._id}
                      value={props.selectValue}
                      checked={drpItem.title == props.selectValue}
                      onChange={(e) =>
                        props.setGenPallor(e, drpItem.title, props.assessName)
                      }
                    />
                    <label className="for-checkbox-tools" htmlFor={drpItem._id}>
                      <span className={props.isChecked(drpItem)}>
                        {drpItem.title}
                      </span>
                    </label>
                  </React.Fragment>
                ))}
              </>
            )}
          {props.assessName == "Cooperative" &&
            formItem.groupName == "Cooperative" && (
              <>
                {formItem.elements.map((drpItem, drpIndex) => (
                  <React.Fragment key={drpIndex}>
                    <input
                      className="checkbox-tools"
                      type="checkbox"
                      name="Conscious"
                      id={drpItem._id}
                      value={props.selectValue}
                      checked={drpItem.title == props.selectValue}
                      onChange={(e) =>
                        props.setGenPallor(e, drpItem.title, props.assessName)
                      }
                    />
                    <label className="for-checkbox-tools" htmlFor={drpItem._id}>
                      <span className={props.isChecked(drpItem)}>
                        {drpItem.title}
                      </span>
                    </label>
                  </React.Fragment>
                ))}
              </>
            )}
          {props.assessName == "Comfortable" &&
            formItem.groupName == "Comfortable" && (
              <>
                {formItem.elements.map((drpItem, drpIndex) => (
                  <React.Fragment key={drpIndex}>
                    <input
                      className="checkbox-tools"
                      type="checkbox"
                      name="Conscious"
                      id={drpItem._id}
                      value={props.selectValue}
                      checked={drpItem.title == props.selectValue}
                      onChange={(e) =>
                        props.setGenPallor(e, drpItem.title, props.assessName)
                      }
                    />
                    <label className="for-checkbox-tools" htmlFor={drpItem._id}>
                      <span className={props.isChecked(drpItem)}>
                        {drpItem.title}
                      </span>
                    </label>
                  </React.Fragment>
                ))}
              </>
            )}
          {props.assessName == "Toxic" && formItem.groupName == "Toxic" && (
            <>
              {formItem.elements.map((drpItem, drpIndex) => (
                <React.Fragment key={drpIndex}>
                  <input
                    className="checkbox-tools"
                    type="checkbox"
                    name="Conscious"
                    id={drpItem._id}
                    value={props.selectValue}
                    checked={drpItem.title == props.selectValue}
                    onChange={(e) =>
                      props.setGenPallor(e, drpItem.title, props.assessName)
                    }
                  />
                  <label className="for-checkbox-tools" htmlFor={drpItem._id}>
                    <span className={props.isChecked(drpItem)}>
                      {drpItem.title}
                    </span>
                  </label>
                </React.Fragment>
              ))}
            </>
          )}
          {props.assessName == "Dyspneic" &&
            formItem.groupName == "Dyspneic" && (
              <>
                {formItem.elements.map((drpItem, drpIndex) => (
                  <React.Fragment key={drpIndex}>
                    <input
                      className="checkbox-tools"
                      type="checkbox"
                      name="Conscious"
                      id={drpItem._id}
                      value={props.selectValue}
                      checked={drpItem.title == props.selectValue}
                      onChange={(e) =>
                        props.setGenPallor(e, drpItem.title, props.assessName)
                      }
                    />
                    <label className="for-checkbox-tools" htmlFor={drpItem._id}>
                      <span className={props.isChecked(drpItem)}>
                        {drpItem.title}
                      </span>
                    </label>
                  </React.Fragment>
                ))}
              </>
            )}
          {props.assessName == "Build" && formItem.groupName == "Build" && (
            <>
              {formItem.elements.map((drpItem, drpIndex) => (
                <React.Fragment key={drpIndex}>
                  <input
                    className="checkbox-tools"
                    type="checkbox"
                    name="Conscious"
                    id={drpItem._id}
                    value={props.selectValue}
                    checked={drpItem.title == props.selectValue}
                    onChange={(e) =>
                      props.setGenPallor(e, drpItem.title, props.assessName)
                    }
                  />
                  <label className="for-checkbox-tools" htmlFor={drpItem._id}>
                    <span className={props.isChecked(drpItem)}>
                      {drpItem.title}
                    </span>
                  </label>
                </React.Fragment>
              ))}
            </>
          )}
          {props.assessName == "Nourishment" &&
            formItem.groupName == "Nourishment" && (
              <>
                {formItem.elements.map((drpItem, drpIndex) => (
                  <React.Fragment key={drpIndex}>
                    <input
                      className="checkbox-tools"
                      type="checkbox"
                      name="Conscious"
                      id={drpItem._id}
                      value={props.selectValue}
                      checked={drpItem.title == props.selectValue}
                      onChange={(e) =>
                        props.setGenPallor(e, drpItem.title, props.assessName)
                      }
                    />
                    <label className="for-checkbox-tools" htmlFor={drpItem._id}>
                      <span className={props.isChecked(drpItem)}>
                        {drpItem.title}
                      </span>
                    </label>
                  </React.Fragment>
                ))}
              </>
            )}
          {props.assessName == "Pallor" && formItem.groupName == "Pallor" && (
            <>
              {formItem.elements.map((drpItem, drpIndex) => (
                <React.Fragment key={drpIndex}>
                  <input
                    className="checkbox-tools"
                    type="checkbox"
                    name="Conscious"
                    id={drpItem._id}
                    value={props.selectValue}
                    checked={drpItem.title == props.selectValue}
                    onChange={(e) =>
                      props.setGenPallor(e, drpItem.title, props.assessName)
                    }
                  />
                  <label className="for-checkbox-tools" htmlFor={drpItem._id}>
                    <span className={props.isChecked(drpItem)}>
                      {drpItem.title}
                    </span>
                  </label>
                </React.Fragment>
              ))}
            </>
          )}
          {props.assessName == "Icterus" && formItem.groupName == "Icterus" && (
            <>
              {formItem.elements.map((drpItem, drpIndex) => (
                <React.Fragment key={drpIndex}>
                  <input
                    className="checkbox-tools"
                    type="checkbox"
                    name="Conscious"
                    id={drpItem._id}
                    value={props.selectValue}
                    checked={drpItem.title == props.selectValue}
                    onChange={(e) =>
                      props.setGenPallor(e, drpItem.title, props.assessName)
                    }
                  />
                  <label className="for-checkbox-tools" htmlFor={drpItem._id}>
                    <span className={props.isChecked(drpItem)}>
                      {drpItem.title}
                    </span>
                  </label>
                </React.Fragment>
              ))}
            </>
          )}
          {props.assessName == "Cyanosis" &&
            formItem.groupName == "Cyanosis" && (
              <>
                {formItem.elements.map((drpItem, drpIndex) => (
                  <React.Fragment key={drpIndex}>
                    <input
                      className="checkbox-tools"
                      type="checkbox"
                      name="Conscious"
                      id={drpItem._id}
                      value={props.selectValue}
                      checked={drpItem.title == props.selectValue}
                      onChange={(e) =>
                        props.setGenPallor(e, drpItem.title, props.assessName)
                      }
                    />
                    <label className="for-checkbox-tools" htmlFor={drpItem._id}>
                      <span className={props.isChecked(drpItem)}>
                        {drpItem.title}
                      </span>
                    </label>
                  </React.Fragment>
                ))}
              </>
            )}
          {props.assessName == "Clubbing" &&
            formItem.groupName == "Clubbing" && (
              <>
                {formItem.elements.map((drpItem, drpIndex) => (
                  <React.Fragment key={drpIndex}>
                    <input
                      className="checkbox-tools"
                      type="checkbox"
                      name="Conscious"
                      id={drpItem._id}
                      value={props.selectValue}
                      checked={drpItem.title == props.selectValue}
                      onChange={(e) =>
                        props.setGenPallor(e, drpItem.title, props.assessName)
                      }
                    />
                    <label className="for-checkbox-tools" htmlFor={drpItem._id}>
                      <span className={props.isChecked(drpItem)}>
                        {drpItem.title}
                      </span>
                    </label>
                  </React.Fragment>
                ))}
              </>
            )}
          {props.assessName == "Koilonychia" &&
            formItem.groupName == "Koilonychia" && (
              <>
                {formItem.elements.map((drpItem, drpIndex) => (
                  <React.Fragment key={drpIndex}>
                    <input
                      className="checkbox-tools"
                      type="checkbox"
                      name="Conscious"
                      id={drpItem._id}
                      value={props.selectValue}
                      checked={drpItem.title == props.selectValue}
                      onChange={(e) =>
                        props.setGenPallor(e, drpItem.title, props.assessName)
                      }
                    />
                    <label className="for-checkbox-tools" htmlFor={drpItem._id}>
                      <span className={props.isChecked(drpItem)}>
                        {drpItem.title}
                      </span>
                    </label>
                  </React.Fragment>
                ))}
              </>
            )}
          {props.assessName == "Lymphadenopathy" &&
            formItem.groupName == "Lymphadenopathy" && (
              <>
                {formItem.elements.map((drpItem, drpIndex) => (
                  <React.Fragment key={drpIndex}>
                    <input
                      className="checkbox-tools"
                      type="checkbox"
                      name="Conscious"
                      id={drpItem._id}
                      value={props.selectValue}
                      checked={drpItem.title == props.selectValue}
                      onChange={(e) =>
                        props.setGenPallor(e, drpItem.title, props.assessName)
                      }
                    />
                    <label className="for-checkbox-tools" htmlFor={drpItem._id}>
                      <span className={props.isChecked(drpItem)}>
                        {drpItem.title}
                      </span>
                    </label>
                  </React.Fragment>
                ))}
              </>
            )}
          {props.assessName == "Pedal Edema" &&
            formItem.groupName == "Pedal Edema" && (
              <>
                {formItem.elements.map((drpItem, drpIndex) => (
                  <React.Fragment key={drpIndex}>
                    <input
                      className="checkbox-tools"
                      type="checkbox"
                      name="Conscious"
                      id={drpItem._id}
                      value={props.selectValue}
                      checked={drpItem.title == props.selectValue}
                      onChange={(e) =>
                        props.setGenPallor(e, drpItem.title, props.assessName)
                      }
                    />
                    <label className="for-checkbox-tools" htmlFor={drpItem._id}>
                      <span className={props.isChecked(drpItem)}>
                        {drpItem.title}
                      </span>
                    </label>
                  </React.Fragment>
                ))}
              </>
            )}
        </React.Fragment>
      ))}
    </div>
  );
}

export default GeneralRadioButton;
