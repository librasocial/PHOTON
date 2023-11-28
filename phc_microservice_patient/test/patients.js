const mongoose = require("mongoose");
const patients = require("../app/models/patients");
const chai = require("chai");
const chaiHttp = require("chai-http");
const app = require("../app");
const { describe, it } = require("mocha");
const should = chai.should();
chai.use(chaiHttp);

describe("Patient", () => {
	let patientId = "";
	let conditionId = "";

	before((done) => {
		patients.remove({}, (err) => {
			done();
		});
	});

	describe("/POST patient", () => {
		it("it should not create new patient without salutation", (done) => {
			let patient = {
				citizen: {
					healthId: "String",
					firstName: "kjb",
					mobileNumber: 1234567891,
				},
			};
			chai
				.request(app)
				.post("/patients")
				.send(patient)
				.end((err, res) => {
					res.should.have.status(400);
					res.body.should.be.a("object");
					res.body.should.have.property("errors");
					res.body.errors[1].should.have
						.property("field")
						.eql("citizen.salutation");
					done();
				});
		});
		it("it should not create new patient without firstName", (done) => {
			let patient = {
				citizen: {
					healthId: "String",
					salutation: "Mr",
					mobileNumber: 1234567891,
				},
			};
			chai
				.request(app)
				.post("/patients")
				.send(patient)
				.end((err, res) => {
					res.should.have.status(400);
					res.body.should.be.a("object");
					res.body.should.have.property("errors");
					res.body.errors[1].should.have
						.property("field")
						.eql("citizen.firstName");
					done();
				});
		});
		it("it should not create new patient without DOB", (done) => {
			let patient = {
				citizen: {
					healthId: "String",
					salutation: "Mr",
					firstName: "ununu",
					lastName: "String",
				},
				registeredOn: "2011-10-05T14:48:00.000Z",
			};
			chai
				.request(app)
				.post("/patients")
				.send(patient)
				.end((err, res) => {
					res.should.have.status(400);
					res.body.should.be.a("object");
					res.body.should.have.property("errors");
					res.body.errors[1].should.have
						.property("field")
						.eql("citizen.dateOfBirth");
					done();
				});
		});
		it("it should create new patient when all inputs are valid", (done) => {
			let patient = {
				lgdCode: "vlgCode",
				citizen: {
					healthId: "string",
					salutation: "string",
					firstName: "string",
					middleName: "string",
					lastName: "string",
					photoUrl: "string",
					mobile: "string",
					residentType: "Resident",
					gender: "string",
					dateOfBirth: "2022-03-07T00:00:00.000Z",
					age: 0,
					maritalStatus: "string",
					religion: "string",
					caste: "string",
					occupation: "string",
					fatherName: "string",
					spouseName: "string",
					address: {
						present: {
							addressLine: "string",
							area: "string",
							village: "string",
							state: "string",
							country: "string",
							pinCode: "string",
							district: "string"
						},
						permanentSameAsPresent: true,
					},
				},
				citizenMoreInfo: {
					birthPlace: "string",
					birthIdentifiers: ["string"],
					annualIncome: 0,
					education: "string",
					primaryLanguage: "string",
					speakEnglish: true,
					phone: "string",
					emailId: "string",
				},
				registeredOn: "2022-03-07T04:39:49.468Z",
				UHId: "vlgCode_95438",
				audit: {
					dateCreated: "2022-04-19T17:05:46.643Z",
					createdBy: "string",
					dateModified: "2022-04-19T17:05:46.643Z",
					modifiedBy: "string",
				},
			};
			chai
				.request(app)
				.post("/patients")
				.send(patient)
				.end((err, res) => {
					patientId = res.body._id;
					res.should.have.status(200);
					res.body.should.be.a("object");
					res.body.should.have.property("citizen");
					res.body.citizen.should.be.a("object");
					res.body.citizen.should.have.property("salutation");
					res.body.citizen.should.have.property("firstName");
					res.body.citizen.should.have.property("lastName");
					res.body.citizen.should.have.property("address");
					done();
				});
		});
	});
	//start of retrievePatient test cases
	describe("/GET/:patientId Patient", () => {
		it("it should GET a patient by the given id", (done) => {
			chai
				.request(app)
				.get(`/patients/${patientId}`)
				.end((err, res) => {
					res.should.have.status(200);
					res.body.should.be.a("object");
					res.body.should.have.property("_id").eql(patientId);
					res.body.should.have.property("citizen");
					res.body.citizen.should.be.a("object");
					res.body.citizen.should.have.property("firstName");
					res.body.citizen.should.have.property("dateOfBirth");
					res.body.should.have.property("audit");
					done();
				});
		});
		it("it should not fetch a patient if the given id is invalid", (done) => {
			chai
				.request(app)
				.get("/patients/622aed0ec557f9203baccd10")
				.end((err, res) => {
					res.should.have.status(404);
					res.body.should.be.a("object");
					res.body.should.have.property("errors");
					res.body.errors[0].should.have.property("field").eql("_id");
					done();
				});
		});
	});
	//end of retrievePatient test cases

	//start of patchPatient test cases
	describe("/PATCH/:patientId updatedPatient", () => {
		it("it should fetch a patient by the given id", (done) => {
			chai
				.request(app)
				.patch(`/patients/${patientId}`)
				.end((err, res) => {
					res.should.have.status(200);
					res.body.should.be.a("object");
					res.body.should.have.property("_id").eql(patientId);
					res.body.should.have.property("citizen");
					res.body.citizen.should.be.a("object");
					res.body.should.have.property("audit");
					done();
				});
		});
		it("it should not fetch a patient if the given id is invalid", (done) => {
			chai
				.request(app)
				.patch("/patients/jgj")
				.end((err, res) => {
					res.should.have.status(400);
					res.body.should.be.a("object");
					res.body.should.have.property("errors");
					res.body.errors[0].should.have.property("field").eql("_id");
					done();
				});
		});
		it("it should update the keys with respective values entered", (done) => {
			chai
				.request(app)
				.patch(`/patients/${patientId}`)
				.end((err, res) => {
					res.should.have.status(200);
					res.body.should.be.a("object");
					res.body.should.have.property("_id").eql(patientId);
					res.body.should.have.property("citizen");
					res.body.citizen.should.be.a("object");
					res.body.should.have.property("audit");
					done();
				});
		});
	});
	//end of patchPatient test cases

	//start of filterPatient test cases

	describe("/GET/filter filterPatients", () => {
		it("it should not GET patient with invalid or no value for skip", (done) => {
			chai
				.request(app)
				.get(
					"/patients/filter?size=4d&page=1&citizen.healthId=string&registeredOn=2022-02-02T11:42:15.985Z&phcOrgId=507f1f77bcf86cd799439011&subCenterOrgId=507f1f77bcf86cd799439011"
				)
				.end((err, res) => {
					// console.log(res.body);
					res.should.have.status(206);
					res.body.should.be.a("object");
					res.body.should.have.property("errors");
					res.body.errors[0].should.have.property("field").eql("size");
					done();
				});
		});
		it("it should not GET patient with no value for page", (done) => {
			chai
				.request(app)
				.get(
					"/patients/filter?size=4&citizen.healthId=string&registeredOn=2022-02-02T11:42:15.985Z&phcOrgId=507f1f77bcf86cd799439011&subCenterOrgId=507f1f77bcf86cd799439011"
				)
				.end((err, res) => {
					// console.log(res.body);
					res.should.have.status(206);
					res.body.should.be.a("object");
					res.body.should.have.property("errors");
					res.body.errors[0].should.have.property("field").eql("page");
					done();
				});
		});
	});

	//end of filterPatient test cases

	//start of test cases for creating health conditions

	describe("/POST/:patientId/healthConditions HealthCondition", () => {
		it("it should create new health condition for a patient", (done) => {
			let dets = {
				healthId: "string",
				patientId: patientId,
				healthConditions: [
					{
						associatedAnatomy: "string",
						signOrSymptom: "string",
						possibleComplication: "string",
						recordedOn: "2022-02-08T06:04:02.560Z",
					},
				],
			};
			chai
				.request(app)
				.post(`/patients/${patientId}/healthConditions`)
				.send(dets)
				.end((err, res) => {
					// console.log(res.body);
					conditionId = res.body._id;
					res.should.have.status(200);
					res.body.should.be.a("object");
					res.body.should.have.property("patientId").eql(patientId);
					res.body.should.have.property("healthConditions");
					done();
				});
		});
	});

	//end of test cases for creating health conditions

	//start of retrievePatient test cases

	describe("/GET/:patientId/healthConditions HealthCondition", () => {
		it("it should GET a patient by the given id", (done) => {
			chai
				.request(app)
				.get(`/patients/${patientId}/healthConditions`)
				.end((err, res) => {
					res.should.have.status(200);
					res.body.should.be.a("object");
					res.body.should.have.property("data");
					res.body.data[0].should.have.property("_id");
					res.body.data[0].should.have.property("patientId").eql(patientId);
					res.body.data[0].should.have.property("audit");
					done();
				});
		});
	});

	//end of retrievePatient test cases

	//start of patchHealthCondition test cases

	describe("/PATCH/:patientId/healthConditions/:conditionId updatedHealthCondition", () => {
		it("it should fetch a patient by the given id", (done) => {
			chai
				.request(app)
				.patch(`/patients/${patientId}/healthConditions/${conditionId}`)
				.end((err, res) => {
					// console.log(res);
					res.should.have.status(200);
					res.body.should.be.a("object");
					res.body.should.have.property("patientId").eql(patientId);
					res.body.should.have.property("healthConditions");
					res.body.should.have.property("audit");
					done();
				});
		});
		it("it should not fetch a patient if the given id is invalid", (done) => {
			chai
				.request(app)
				.patch("/patients/jgj/healthConditions/gijnkm")
				.end((err, res) => {
					res.should.have.status(400);
					res.body.should.be.a("object");
					res.body.should.have.property("errors");
					res.body.errors[0].should.have.property("field").eql("patientId");
					done();
				});
		});
		it("it should update the keys with respective values entered", (done) => {
			chai
				.request(app)
				.patch(`/patients/${patientId}/healthConditions/${conditionId}`)
				.end((err, res) => {
					res.should.have.status(200);
					res.body.should.be.a("object");
					res.body.should.have.property("patientId").eql(patientId);
					res.body.should.have.property("healthConditions");
					res.body.should.have.property("audit");
					done();
				});
		});
	});

	//end of patchHealthCondition test cases

	//start of searchPatient test cases

	describe("/GET/search Patients", () => {
		it("it should GET patient if valid information is provided", (done) => {
			chai
				.request(app)
				.get(
					"/patients/search?fieldName=UHID&value=62209ad3918e824c0371fb7a&page=1"
				)
				.end((err, res) => {
					// console.log(res.body);
					res.should.have.status(200);
					res.body.should.be.a("object");
					res.body.should.have.property("data");
					res.body.should.have.property("meta");
					done();
				});
		});
		it("it should GET patient if valid information is provided", (done) => {
			chai
				.request(app)
				.get("/patients/search?fieldName=Name&value=updated name&page=1")
				.end((err, res) => {
					// console.log(res.body);
					res.should.have.status(200);
					res.body.should.be.a("object");
					res.body.should.have.property("data");
					res.body.should.have.property("meta");
					done();
				});
		});
		it("it should GET patient if valid information is provided", (done) => {
			chai
				.request(app)
				.get(
					"/patients/search?fieldName=DateOfBirth&value=2022-03-02T11:42:15.985Z&page=1"
				)
				.end((err, res) => {
					// console.log(res.body);
					res.should.have.status(200);
					res.body.should.be.a("object");
					res.body.should.have.property("data");
					res.body.should.have.property("meta");
					done();
				});
		});
		it("it should GET patient if valid information is provided", (done) => {
			chai
				.request(app)
				.get("/patients/search?fieldName=MobileNumber&value=6767676767&page=1")
				.end((err, res) => {
					// console.log(res.body);
					res.should.have.status(200);
					res.body.should.be.a("object");
					res.body.should.have.property("data");
					res.body.should.have.property("meta");
					done();
				});
		});
	});

	//end of searchPatient test cases

	after((done) => {
		patients.remove(() => {
			done();
		});
	});
});
