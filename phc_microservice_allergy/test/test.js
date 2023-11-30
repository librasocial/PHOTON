const mongoose = require("mongoose");
const AllergyModel = require("../app/models/AllergyModel");
const chai = require("chai");
const chaiHttp = require("chai-http");
const app = require("../app");
const { describe, it } = require("mocha");
const should = chai.should();
chai.use(chaiHttp);

describe("Allergy", () => {
	let allergyId = "";
	let encounterId = "";
	let UHId = "";
	let encounterDate = "";

	before((done) => {
		AllergyModel.remove({}, (err) => {
			done();
		});
	});

	describe("/POST Allergy", () => {
		it("it should create a new Allergy", (done) => {
			const allergy = {
				UHId: "string",
				encounterId: "string",
				encounterDate: "2022-05-12T10:36:41.122Z",
				patientId: "string",
				recordedBy: "staff Id of the nurse of ANM who is recording it",
				doctor: "staff Id of the medical officer",
				category: "Drug",
				allergen: "string",
				reactionType: "Adverse",
				reaction: "string",
				confirmationType: "Sure",
				dateSince: "2022-05-12T10:36:41.122Z",
				onsetDate: "2022-05-12T10:36:41.122Z",
				status: "Open",
				severity: "Mild",
				infoSource: "string",
				reactionSite: "the body part site",
				relievingFactor: "string",
				closureDate: "2022-05-12T10:36:41.122Z",
			};

			chai
				.request(app)
				.post("/allergies")
				.send(allergy)
				.end((err, res) => {
					allergyId = res.body._id;
					res.should.have.status(201);
					res.body.should.be.a("object");
					res.body.should.have.property("UHId");
					res.body.UHId.should.be.a("string");
					res.body.should.have.property("encounterId");
					res.body.encounterId.should.be.a("string");
					res.body.should.have.property("encounterDate");
					res.body.encounterDate.should.be.a("String");
					res.body.should.have.property("patientId");
					res.body.patientId.should.be.a("string");
					res.body.should.have.property("doctor");
					res.body.doctor.should.be.a("string");
					res.body.should.have.property("category");
					res.body.category.should.be.a("string");
					res.body.should.have.property("allergen");
					res.body.allergen.should.be.a("string");
					res.body.should.have.property("reactionType");
					res.body.reactionType.should.be.a("string");
					res.body.should.have.property("reaction");
					res.body.reaction.should.be.a("string");
					res.body.should.have.property("confirmationType");
					res.body.confirmationType.should.be.a("string");
					res.body.should.have.property("dateSince");
					res.body.dateSince.should.be.a("String");
					res.body.should.have.property("onsetDate");
					res.body.onsetDate.should.be.a("String");
					res.body.should.have.property("status");
					res.body.status.should.be.a("string");
					res.body.should.have.property("severity");
					res.body.severity.should.be.a("string");
					res.body.should.have.property("infoSource");
					res.body.infoSource.should.be.a("string");
					res.body.should.have.property("reactionSite");
					res.body.reactionSite.should.be.a("string");
					res.body.should.have.property("relievingFactor");
					res.body.relievingFactor.should.be.a("string");
					res.body.should.have.property("closureDate");
					res.body.closureDate.should.be.a("String");
					res.body.should.have.property("audit");
					res.body.audit.should.be.a("object");
					res.body.should.have.property("_id");
					res.body._id.should.be.a("string");
					done();
				});
		});
		it("it should not create a new allergy if encounterDate is not of Date type", (done) => {
			chai
				.request(app)
				.post("/allergies")
				.send({
					UHId: "string",
					encounterId: "string",
					encounterDate: 12345,
					patientId: "string",
					recordedBy: "staff Id of the nurse of ANM who is recording it",
					doctor: "staff Id of the medical officer",
					category: "Drug",
					allergen: "string",
					reactionType: "Adverse",
					reaction: "string",
					confirmationType: "Sure",
					dateSince: "2022-05-12T10:36:41.122Z",
					onsetDate: "2022-05-12T10:36:41.122Z",
					status: "Open",
					severity: "Mild",
					infoSource: "string",
					reactionSite: "the body part site",
					relievingFactor: "string",
					closureDate: "2022-05-12T10:36:41.122Z",
				})
				.end((err, res) => {
					res.should.have.status(400);
					res.body.should.be.a("object");
					res.body.should.have.property("errors");
					res.body.errors.should.be.a("array");
					res.body.errors[0].should.have.property("field").eql("encounterDate");
					res.body.errors[0].should.have
						.property("message")
						.eql("Invalid value");
					done();
				});
		});
		it("it should not create a new allergy if doctor is not of string type", (done) => {
			chai
				.request(app)
				.post("/allergies")
				.send({
					UHId: "string",
					encounterId: "string",
					encounterDate: "2022-05-12T10:36:41.122Z",
					patientId: "string",
					recordedBy: "staff Id of the nurse of ANM who is recording it",
					doctor: 12345,
					category: "Drug",
					allergen: "string",
					reactionType: "Adverse",
					reaction: "string",
					confirmationType: "Sure",
					dateSince: "2022-05-12T10:36:41.122Z",
					onsetDate: "2022-05-12T10:36:41.122Z",
					status: "Open",
					severity: "Mild",
					infoSource: "string",
					reactionSite: "the body part site",
					relievingFactor: "string",
					closureDate: "2022-05-12T10:36:41.122Z",
				})
				.end((err, res) => {
					res.should.have.status(400);
					res.body.should.be.a("object");
					res.body.should.have.property("errors");
					res.body.errors.should.be.a("array");
					res.body.errors[0].should.have.property("field").eql("doctor");
					res.body.errors[0].should.have
						.property("message")
						.eql("Invalid value");
					done();
				});
		});
	});

	describe("/GET Allergy", () => {
		it("it should not fetch the Allergy", (done) => {
			chai
				.request(app)
				.get("/allergies/6270ebd3768b9395e0111111")
				.end((err, res) => {
					res.should.have.status(404);
					res.body.should.be.a("object");
					res.body.should.have.property("errors");
					res.body.errors.should.be.a("array");
					res.body.errors[0].should.have.property("field").eql("allergyId");
					res.body.errors[0].should.have
						.property("message")
						.eql("Allergy not found for 6270ebd3768b9395e0111111");
					done();
				});
		});
		it("it should fetch the Allergy", (done) => {
			chai
				.request(app)
				.get(`/allergies/${allergyId}`)
				.end((err, res) => {
					res.should.have.status(200);
					res.body.should.be.a("object");
					res.body.should.have.property("_id").eql(allergyId);
					res.body.should.be.a("object");
					res.body.should.have.property("UHId");
					res.body.UHId.should.be.a("string");
					res.body.should.have.property("encounterId");
					res.body.encounterId.should.be.a("string");
					res.body.should.have.property("encounterDate");
					res.body.encounterDate.should.be.a("String");
					res.body.should.have.property("patientId");
					res.body.patientId.should.be.a("string");
					res.body.should.have.property("doctor");
					res.body.doctor.should.be.a("string");
					res.body.should.have.property("category");
					res.body.category.should.be.a("string");
					res.body.should.have.property("allergen");
					res.body.allergen.should.be.a("string");
					res.body.should.have.property("reactionType");
					res.body.reactionType.should.be.a("string");
					res.body.should.have.property("reaction");
					res.body.reaction.should.be.a("string");
					res.body.should.have.property("confirmationType");
					res.body.confirmationType.should.be.a("string");
					res.body.should.have.property("dateSince");
					res.body.dateSince.should.be.a("String");
					res.body.should.have.property("onsetDate");
					res.body.onsetDate.should.be.a("String");
					res.body.should.have.property("status");
					res.body.status.should.be.a("string");
					res.body.should.have.property("severity");
					res.body.severity.should.be.a("string");
					res.body.should.have.property("infoSource");
					res.body.infoSource.should.be.a("string");
					res.body.should.have.property("reactionSite");
					res.body.reactionSite.should.be.a("string");
					res.body.should.have.property("relievingFactor");
					res.body.relievingFactor.should.be.a("string");
					res.body.should.have.property("closureDate");
					res.body.closureDate.should.be.a("String");
					res.body.should.have.property("audit");
					res.body.audit.should.be.a("object");
					done();
				});
		});
	});

	describe("/PATCH Allergy", () => {
		it("it should not update the Allergy", (done) => {
			chai
				.request(app)
				.patch(`/allergies/6270ebd3768b9395e0111111`)
				.send({
					recordedBy: "staff Id",
					doctor: "staff Id",
				})
				.end((err, res) => {
					res.should.have.status(404);
					res.body.should.be.a("object");
					res.body.should.have.property("errors");
					res.body.errors.should.be.a("array");
					res.body.errors[0].should.have.property("field").eql("allergyId");
					res.body.errors[0].should.have
						.property("message")
						.eql("Allergy not found for 6270ebd3768b9395e0111111");
					done();
				});
		});
		it("it should update the Allergy", (done) => {
			chai
				.request(app)
				.patch(`/allergies/${allergyId}`)
				.send({
					recordedBy: "staff Id",
					doctor: "staff Id",
				})
				.end((err, res) => {
					res.should.have.status(200);
					res.body.should.be.a("object");
					res.body.should.have.property("UHId");
					res.body.UHId.should.be.a("string");
					res.body.should.have.property("encounterId");
					res.body.encounterId.should.be.a("string");
					res.body.should.have.property("encounterDate");
					res.body.encounterDate.should.be.a("String");
					res.body.should.have.property("patientId");
					res.body.patientId.should.be.a("string");
					res.body.should.have.property("doctor");
					res.body.doctor.should.be.a("string");
					res.body.should.have.property("category");
					res.body.category.should.be.a("string");
					res.body.should.have.property("allergen");
					res.body.allergen.should.be.a("string");
					res.body.should.have.property("reactionType");
					res.body.reactionType.should.be.a("string");
					res.body.should.have.property("reaction");
					res.body.reaction.should.be.a("string");
					res.body.should.have.property("confirmationType");
					res.body.confirmationType.should.be.a("string");
					res.body.should.have.property("dateSince");
					res.body.dateSince.should.be.a("String");
					res.body.should.have.property("onsetDate");
					res.body.onsetDate.should.be.a("String");
					res.body.should.have.property("status");
					res.body.status.should.be.a("string");
					res.body.should.have.property("severity");
					res.body.severity.should.be.a("string");
					res.body.should.have.property("infoSource");
					res.body.infoSource.should.be.a("string");
					res.body.should.have.property("reactionSite");
					res.body.reactionSite.should.be.a("string");
					res.body.should.have.property("relievingFactor");
					res.body.relievingFactor.should.be.a("string");
					res.body.should.have.property("closureDate");
					res.body.closureDate.should.be.a("String");
					res.body.should.have.property("audit");
					res.body.audit.should.be.a("object");
					res.body.should.have.property("_id");
					res.body._id.should.be.a("string");
					done();
				});
		});
	});

	describe("/GET filter Allergy", () => {
		it("it should filter the Allergy if page, size and encounterId is provided", (done) => {
			chai
				.request(app)
				.get(`/allergies/filter?page=1&size=2&encounterId=${encounterId}`)
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
					res.body.data[0].should.have.property("encounterDate");
					res.body.data[0].encounterDate.should.be.a("String");
					res.body.data[0].should.have.property("patientId");
					res.body.data[0].patientId.should.be.a("string");
					res.body.data[0].should.have.property("doctor");
					res.body.data[0].doctor.should.be.a("string");
					res.body.data[0].should.have.property("category");
					res.body.data[0].category.should.be.a("string");
					res.body.data[0].should.have.property("allergen");
					res.body.data[0].allergen.should.be.a("string");
					res.body.data[0].should.have.property("reactionType");
					res.body.data[0].reactionType.should.be.a("string");
					res.body.data[0].should.have.property("reaction");
					res.body.data[0].reaction.should.be.a("string");
					res.body.data[0].should.have.property("confirmationType");
					res.body.data[0].confirmationType.should.be.a("string");
					res.body.data[0].should.have.property("dateSince");
					res.body.data[0].dateSince.should.be.a("String");
					res.body.data[0].should.have.property("onsetDate");
					res.body.data[0].onsetDate.should.be.a("String");
					res.body.data[0].should.have.property("status");
					res.body.data[0].status.should.be.a("string");
					res.body.data[0].should.have.property("severity");
					res.body.data[0].severity.should.be.a("string");
					res.body.data[0].should.have.property("infoSource");
					res.body.data[0].infoSource.should.be.a("string");
					res.body.data[0].should.have.property("reactionSite");
					res.body.data[0].reactionSite.should.be.a("string");
					res.body.data[0].should.have.property("relievingFactor");
					res.body.data[0].relievingFactor.should.be.a("string");
					res.body.data[0].should.have.property("closureDate");
					res.body.data[0].closureDate.should.be.a("String");
					res.body.data[0].should.have.property("audit");
					res.body.data[0].audit.should.be.a("object");
					done();
				});
		});
		it("it should filter the Allergy if page, size and UHId is provided", (done) => {
			chai
				.request(app)
				.get(`/allergies/filter?page=1&size=2&UHId=${UHId}`)
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
					res.body.data[0].should.have.property("encounterDate");
					res.body.data[0].encounterDate.should.be.a("String");
					res.body.data[0].should.have.property("patientId");
					res.body.data[0].patientId.should.be.a("string");
					res.body.data[0].should.have.property("doctor");
					res.body.data[0].doctor.should.be.a("string");
					res.body.data[0].should.have.property("category");
					res.body.data[0].category.should.be.a("string");
					res.body.data[0].should.have.property("allergen");
					res.body.data[0].allergen.should.be.a("string");
					res.body.data[0].should.have.property("reactionType");
					res.body.data[0].reactionType.should.be.a("string");
					res.body.data[0].should.have.property("reaction");
					res.body.data[0].reaction.should.be.a("string");
					res.body.data[0].should.have.property("confirmationType");
					res.body.data[0].confirmationType.should.be.a("string");
					res.body.data[0].should.have.property("dateSince");
					res.body.data[0].dateSince.should.be.a("String");
					res.body.data[0].should.have.property("onsetDate");
					res.body.data[0].onsetDate.should.be.a("String");
					res.body.data[0].should.have.property("status");
					res.body.data[0].status.should.be.a("string");
					res.body.data[0].should.have.property("severity");
					res.body.data[0].severity.should.be.a("string");
					res.body.data[0].should.have.property("infoSource");
					res.body.data[0].infoSource.should.be.a("string");
					res.body.data[0].should.have.property("reactionSite");
					res.body.data[0].reactionSite.should.be.a("string");
					res.body.data[0].should.have.property("relievingFactor");
					res.body.data[0].relievingFactor.should.be.a("string");
					res.body.data[0].should.have.property("closureDate");
					res.body.data[0].closureDate.should.be.a("String");
					res.body.data[0].should.have.property("audit");
					res.body.data[0].audit.should.be.a("object");
					done();
				});
		});
		it("it should filter the Allergy if page, size and encounterDate is provided", (done) => {
			chai
				.request(app)
				.get(`/allergies/filter?page=1&size=2&encounterDate=${encounterDate}`)
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
					res.body.data[0].should.have.property("encounterDate");
					res.body.data[0].encounterDate.should.be.a("String");
					res.body.data[0].should.have.property("patientId");
					res.body.data[0].patientId.should.be.a("string");
					res.body.data[0].should.have.property("doctor");
					res.body.data[0].doctor.should.be.a("string");
					res.body.data[0].should.have.property("category");
					res.body.data[0].category.should.be.a("string");
					res.body.data[0].should.have.property("allergen");
					res.body.data[0].allergen.should.be.a("string");
					res.body.data[0].should.have.property("reactionType");
					res.body.data[0].reactionType.should.be.a("string");
					res.body.data[0].should.have.property("reaction");
					res.body.data[0].reaction.should.be.a("string");
					res.body.data[0].should.have.property("confirmationType");
					res.body.data[0].confirmationType.should.be.a("string");
					res.body.data[0].should.have.property("dateSince");
					res.body.data[0].dateSince.should.be.a("String");
					res.body.data[0].should.have.property("onsetDate");
					res.body.data[0].onsetDate.should.be.a("String");
					res.body.data[0].should.have.property("status");
					res.body.data[0].status.should.be.a("string");
					res.body.data[0].should.have.property("severity");
					res.body.data[0].severity.should.be.a("string");
					res.body.data[0].should.have.property("infoSource");
					res.body.data[0].infoSource.should.be.a("string");
					res.body.data[0].should.have.property("reactionSite");
					res.body.data[0].reactionSite.should.be.a("string");
					res.body.data[0].should.have.property("relievingFactor");
					res.body.data[0].relievingFactor.should.be.a("string");
					res.body.data[0].should.have.property("closureDate");
					res.body.data[0].closureDate.should.be.a("String");
					res.body.data[0].should.have.property("audit");
					res.body.data[0].audit.should.be.a("object");
					done();
				});
		});
	});

	after((done) => {
		AllergyModel.remove(() => {
			done();
		});
	});
});
