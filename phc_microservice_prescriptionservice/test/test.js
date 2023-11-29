const mongoose = require("mongoose");
const PrescriptionModel = require("../app/models/PrescriptionModel");
const chai = require("chai");
const chaiHttp = require("chai-http");
const app = require("../app");
const { describe, it } = require("mocha");
const should = chai.should();
chai.use(chaiHttp);

describe("Prescription", () => {
	let prescriptionId = "";
	let encounterId = "";
	let UHId = "";
	let encounterDate = "";

	before((done) => {
		PrescriptionModel.remove({}, (err) => {
			done();
		});
	});

	describe("/POST Prescription", () => {
		it("it should create a new prescription", (done) => {
			let prescription = {
				UHId: "string",
				encounterId: "string",
				patientId: "string",
				prescribedBy: "staff id of doctor",
				prescribedOn: "2022-05-09T05:45:45.941Z",
				partOfPrescription: "string",
				medicalTests: ["string"],
				instruction: "string",
				reviewAfter: "P15D (iso duration in string)",
				drugs: [
					{
						name: "medicine name",
						strength: "power eg. 500mg",
						route: ["oral"],
						form: "capsule",
						frequency: ["morning", "night"],
						duration: "P15D",
						timing: ["after food"],
						startDate: "2022-05-09T05:45:45.941Z",
					},
				],
			};

			chai
				.request(app)
				.post("/prescriptions")
				.send(prescription)
				.end((err, res) => {
					prescriptionId = res.body._id;
					encounterId = res.body.encounterId;
					UHId = res.body.UHId;
					encounterDate = res.body.prescribedOn;
					res.should.have.status(201);
					res.body.should.be.a("object");
					res.body.should.have.property("UHId");
					res.body.UHId.should.be.a("string");
					res.body.should.have.property("encounterId");
					res.body.encounterId.should.be.a("string");
					res.body.should.have.property("patientId");
					res.body.patientId.should.be.a("string");
					res.body.should.have.property("prescribedBy");
					res.body.prescribedBy.should.be.a("string");
					res.body.should.have.property("prescribedOn");
					res.body.prescribedOn.should.be.a("string");
					res.body.should.have.property("partOfPrescription");
					res.body.partOfPrescription.should.be.a("string");
					res.body.should.have.property("medicalTests");
					res.body.medicalTests.should.be.a("array");
					res.body.should.have.property("instruction");
					res.body.instruction.should.be.a("string");
					res.body.should.have.property("reviewAfter");
					res.body.reviewAfter.should.be.a("string");
					res.body.should.have.property("drugs");
					res.body.drugs.should.be.a("array");
					res.body.should.have.property("_id");
					done();
				});
		});
		it("it should not create a new prescription", (done) => {
			let prescription = {
				UHId: "string",
				encounterId: "string",
				patientId: "string",
				prescribedBy: "staff id of doctor",
				prescribedOn: "checkkk",
				partOfPrescription: "string",
				medicalTests: ["string"],
				instruction: "string",
				reviewAfter: "P15D (iso duration in string)",
				drugs: [
					{
						name: "medicine name",
						strength: "power eg. 500mg",
						route: ["oral"],
						form: "capsule",
						frequency: ["morning", "night"],
						duration: "P15D",
						timing: ["after food"],
						startDate: "2022-05-09T05:45:45.941Z",
					},
				],
			};

			chai
				.request(app)
				.post("/prescriptions")
				.send(prescription)
				.end((err, res) => {
					res.should.have.status(400);
					res.body.should.be.a("object");
					res.body.should.have.property("errors");
					res.body.errors.should.be.a("array");
					res.body.errors[0].should.have.property("field").eql("prescribedOn");
					res.body.errors[0].should.have
						.property("message")
						.eql("Invalid value");
					done();
				});
		});
		it("it should not create a new prescription if drugs is not an array", (done) => {
			let prescription = {
				UHId: "string",
				encounterId: "string",
				patientId: "string",
				prescribedBy: "staff id of doctor",
				prescribedOn: "2022-05-09T05:45:45.941Z",
				partOfPrescription: "string",
				medicalTests: ["string"],
				instruction: "string",
				reviewAfter: "P15D (iso duration in string)",
				drugs: "string",
			};

			chai
				.request(app)
				.post("/prescriptions")
				.send(prescription)
				.end((err, res) => {
					res.should.have.status(400);
					res.body.should.be.a("object");
					res.body.should.have.property("errors");
					res.body.errors.should.be.a("array");
					res.body.errors[0].should.have.property("field").eql("drugs");
					res.body.errors[0].should.have
						.property("message")
						.eql("Invalid value");
					done();
				});
		});
	});

	describe("/GET/:prescriptionId Prescription", () => {
		it("it should get a prescription", (done) => {
			chai
				.request(app)
				.get(`/prescriptions/${prescriptionId}`)
				.end((err, res) => {
					res.should.have.status(200);
					res.body.should.be.a("object");
					res.body.should.have.property("UHId");
					res.body.UHId.should.be.a("string");
					res.body.should.have.property("encounterId");
					res.body.encounterId.should.be.a("string");
					res.body.should.have.property("patientId");
					res.body.patientId.should.be.a("string");
					res.body.should.have.property("prescribedBy");
					res.body.prescribedBy.should.be.a("string");
					res.body.should.have.property("prescribedOn");
					res.body.prescribedOn.should.be.a("string");
					res.body.should.have.property("partOfPrescription");
					res.body.partOfPrescription.should.be.a("string");
					res.body.should.have.property("medicalTests");
					res.body.medicalTests.should.be.a("array");
					res.body.should.have.property("instruction");
					res.body.instruction.should.be.a("string");
					res.body.should.have.property("reviewAfter");
					res.body.reviewAfter.should.be.a("string");
					res.body.should.have.property("drugs");
					res.body.drugs.should.be.a("array");
					res.body.should.have.property("_id");
					done();
				});
		});
		it("it should not get a prescription", (done) => {
			chai
				.request(app)
				.get("/prescriptions/6278abff415b77ac65531111")
				.end((err, res) => {
					res.should.have.status(404);
					res.body.should.be.a("object");
					res.body.should.have.property("errors");
					res.body.errors.should.be.a("array");
					res.body.errors[0].should.have
						.property("message")
						.eql("Prescription not found for 6278abff415b77ac65531111");
					done();
				});
		});
	});

	describe("PATCH/:prescriptionId Prescription", () => {
		it("it should update a prescription", (done) => {
			let prescription = {
				prescribedBy: "doctorId",
				medicalTests: ["string1", "string2"],
				drugs: [
					{
						strength: "300mg",
						form: ["tablet"],
						duration: "P20D",
					},
				],
			};

			chai
				.request(app)
				.patch(`/prescriptions/${prescriptionId}`)
				.send(prescription)
				.end((err, res) => {
					res.should.have.status(200);
					res.body.should.be.a("object");
					res.body.should.have.property("UHId");
					res.body.UHId.should.be.a("string");
					res.body.should.have.property("encounterId");
					res.body.encounterId.should.be.a("string");
					res.body.should.have.property("patientId");
					res.body.patientId.should.be.a("string");
					res.body.should.have.property("prescribedBy");
					res.body.prescribedBy.should.be.a("string");
					res.body.should.have.property("prescribedOn");
					res.body.prescribedOn.should.be.a("string");
					res.body.should.have.property("partOfPrescription");
					res.body.partOfPrescription.should.be.a("string");
					res.body.should.have.property("medicalTests");
					res.body.medicalTests.should.be.a("array");
					res.body.should.have.property("instruction");
					res.body.instruction.should.be.a("string");
					res.body.should.have.property("reviewAfter");
					res.body.reviewAfter.should.be.a("string");
					res.body.should.have.property("drugs");
					res.body.drugs.should.be.a("array");
					res.body.should.have.property("_id");
					done();
				});
		});
		it("it should not update a prescription", (done) => {
			let prescription = {
				prescribedBy: "doctorId",
				medicalTests: ["string1", "string2"],
				drugs: [
					{
						strength: "300mg",
						form: ["tablet"],
						duration: "P20D",
					},
				],
			};

			chai
				.request(app)
				.patch("/prescriptions/6270ebd3768b9395e0111111")
				.send(prescription)
				.end((err, res) => {
					res.should.have.status(404);
					res.body.should.be.a("object");
					res.body.should.have.property("errors");
					res.body.errors.should.be.a("array");
					res.body.errors[0].should.have
						.property("field")
						.eql("prescriptionId");
					res.body.errors[0].should.have
						.property("message")
						.eql("Prescription not found for _id 6270ebd3768b9395e0111111");
					done();
				});
		});
	});

	describe("/GET Prescriptions", () => {
		it("it should filter the prescriptions if page, size and encounterId is provided", (done) => {
			chai
				.request(app)
				.get(`/prescriptions/filter?page=1&size=5&encounterId=${encounterId}`)
				.end((err, res) => {
					res.should.have.status(200);
					res.body.should.be.a("object");
					res.body.should.have.property("meta");
					res.body.meta.should.be.a("object");
					res.body.meta.should.have.property("totalPages");
					res.body.meta.totalPages.should.be.a("number");
					res.body.meta.should.have.property("totalElements");
					res.body.meta.totalElements.should.be.a("number");
					res.body.meta.should.have.property("size");
					res.body.meta.size.should.be.a("number");
					res.body.meta.should.have.property("number");
					res.body.meta.number.should.be.a("number");
					res.body.should.have.property("data");
					res.body.data.should.be.a("array");
					res.body.data[0].should.have.property("UHId");
					res.body.data[0].UHId.should.be.a("string");
					res.body.data[0].should.have.property("encounterId");
					res.body.data[0].encounterId.should.be.a("string");
					res.body.data[0].should.have.property("patientId");
					res.body.data[0].patientId.should.be.a("string");
					res.body.data[0].should.have.property("prescribedBy");
					res.body.data[0].prescribedBy.should.be.a("string");
					res.body.data[0].should.have.property("prescribedOn");
					res.body.data[0].prescribedOn.should.be.a("string");
					res.body.data[0].should.have.property("partOfPrescription");
					res.body.data[0].partOfPrescription.should.be.a("string");
					res.body.data[0].should.have.property("medicalTests");
					res.body.data[0].medicalTests.should.be.a("array");
					res.body.data[0].should.have.property("instruction");
					res.body.data[0].instruction.should.be.a("string");
					res.body.data[0].should.have.property("reviewAfter");
					res.body.data[0].reviewAfter.should.be.a("string");
					res.body.data[0].should.have.property("drugs");
					res.body.data[0].drugs.should.be.a("array");
					res.body.data[0].should.have.property("_id");
					res.body.data[0]._id.should.be.a("string");
					done();
				});
		});
		it("it should filter the prescriptions if page, size and UHId is provided", (done) => {
			chai
				.request(app)
				.get(`/prescriptions/filter?page=1&size=5&UHId=${UHId}`)
				.end((err, res) => {
					res.should.have.status(200);
					res.body.should.be.a("object");
					res.body.should.have.property("meta");
					res.body.meta.should.be.a("object");
					res.body.meta.should.have.property("totalPages");
					res.body.meta.totalPages.should.be.a("number");
					res.body.meta.should.have.property("totalElements");
					res.body.meta.totalElements.should.be.a("number");
					res.body.meta.should.have.property("size");
					res.body.meta.size.should.be.a("number");
					res.body.meta.should.have.property("number");
					res.body.meta.number.should.be.a("number");
					res.body.should.have.property("data");
					res.body.data.should.be.a("array");
					res.body.data[0].should.have.property("UHId");
					res.body.data[0].UHId.should.be.a("string");
					res.body.data[0].should.have.property("encounterId");
					res.body.data[0].encounterId.should.be.a("string");
					res.body.data[0].should.have.property("patientId");
					res.body.data[0].patientId.should.be.a("string");
					res.body.data[0].should.have.property("prescribedBy");
					res.body.data[0].prescribedBy.should.be.a("string");
					res.body.data[0].should.have.property("prescribedOn");
					res.body.data[0].prescribedOn.should.be.a("string");
					res.body.data[0].should.have.property("partOfPrescription");
					res.body.data[0].partOfPrescription.should.be.a("string");
					res.body.data[0].should.have.property("medicalTests");
					res.body.data[0].medicalTests.should.be.a("array");
					res.body.data[0].should.have.property("instruction");
					res.body.data[0].instruction.should.be.a("string");
					res.body.data[0].should.have.property("reviewAfter");
					res.body.data[0].reviewAfter.should.be.a("string");
					res.body.data[0].should.have.property("drugs");
					res.body.data[0].drugs.should.be.a("array");
					res.body.data[0].should.have.property("_id");
					res.body.data[0]._id.should.be.a("string");
					done();
				});
		});
		it("it should filter the prescriptions if page, size and encounterDate is provided", (done) => {
			chai
				.request(app)
				.get(
					`/prescriptions/filter?page=1&size=5&encounterDate=${encounterDate}`
				)
				.end((err, res) => {
					res.should.have.status(200);
					res.body.should.be.a("object");
					res.body.should.have.property("meta");
					res.body.meta.should.be.a("object");
					res.body.meta.should.have.property("totalPages");
					res.body.meta.totalPages.should.be.a("number");
					res.body.meta.should.have.property("totalElements");
					res.body.meta.totalElements.should.be.a("number");
					res.body.meta.should.have.property("size");
					res.body.meta.size.should.be.a("number");
					res.body.meta.should.have.property("number");
					res.body.meta.number.should.be.a("number");
					res.body.should.have.property("data");
					res.body.data.should.be.a("array");
					res.body.data[0].should.have.property("UHId");
					res.body.data[0].UHId.should.be.a("string");
					res.body.data[0].should.have.property("encounterId");
					res.body.data[0].encounterId.should.be.a("string");
					res.body.data[0].should.have.property("patientId");
					res.body.data[0].patientId.should.be.a("string");
					res.body.data[0].should.have.property("prescribedBy");
					res.body.data[0].prescribedBy.should.be.a("string");
					res.body.data[0].should.have.property("prescribedOn");
					res.body.data[0].prescribedOn.should.be.a("string");
					res.body.data[0].should.have.property("partOfPrescription");
					res.body.data[0].partOfPrescription.should.be.a("string");
					res.body.data[0].should.have.property("medicalTests");
					res.body.data[0].medicalTests.should.be.a("array");
					res.body.data[0].should.have.property("instruction");
					res.body.data[0].instruction.should.be.a("string");
					res.body.data[0].should.have.property("reviewAfter");
					res.body.data[0].reviewAfter.should.be.a("string");
					res.body.data[0].should.have.property("drugs");
					res.body.data[0].drugs.should.be.a("array");
					res.body.data[0].should.have.property("_id");
					res.body.data[0]._id.should.be.a("string");
					done();
				});
		});
	});

	after((done) => {
		PrescriptionModel.remove(() => {
			done();
		});
	});
});
