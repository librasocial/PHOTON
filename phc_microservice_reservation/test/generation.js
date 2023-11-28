const mongoose = require("mongoose");
const reservations = require("../app/models/reservation");
const chai = require("chai");
const chaiHttp = require("chai-http");
const app = require("../app");
const { describe, it } = require("mocha");
const should = chai.should();
chai.use(chaiHttp);

describe("Reservation", () => {
	let providerId = "";
	let reservationId = "";
	let status = "";

	before((done) => {
		reservations.remove({}, (err) => {
			done();
		});
	});

	describe("/POST reservations", () => {
		it("it should not create new patient without patientId", (done) => {
			let Reservation = {
				Patient: {
					healthId: "String",
					dateOfBirth: "2022-01-31",
				},
			};
			chai
				.request(app)
				.post("/reservations")
				.send(Reservation)
				.end((err, res) => {
					res.should.have.status(400);
					res.body.should.be.a("object");
					res.body.should.have.property("errors");
					res.body.errors[0].should.have
						.property("field")
						.eql("Patient.patientId");
					done();
				});
		});
		it("it should not create new reservation without reservationFor", (done) => {
			let Reservation = {
				Patient: {
					patientId: "Check",
					healthId: "String",
					dateOfBirth: "2022-01-31",
				},
				Provider: {
					memberId: "String",
					name: "String",
				},
				reservationTime: "2022-01-31",
				status: "Hold",
				tokenNumber: "String",
			};
			chai
				.request(app)
				.post("/reservations")
				.send(Reservation)
				.end((err, res) => {
					res.should.have.status(400);
					res.body.should.be.a("object");
					res.body.should.have.property("errors");
					res.body.errors[0].should.have
						.property("field")
						.eql("reservationFor");
					done();
				});
		});
		it("it should not create new reservation without audit", (done) => {
			let Reservation = {
				Patient: {
					patientId: "Check",
					healthId: "String",
					dateOfBirth: "2022-01-31",
				},
				Provider: {
					memberId: "String",
					name: "String",
				},
				reservationTime: "2022-01-31",
				status: "Hold",
				tokenNumber: "String",
			};
			chai
				.request(app)
				.post("/reservations")
				.send(Reservation)
				.end((err, res) => {
					res.should.have.status(400);
					res.body.should.be.a("object");
					res.body.should.have.property("errors");
					res.body.errors[0].should.have
						.property("field")
						.eql("reservationFor");
					done();
				});
		});
		it("it should create new reservation when all inputs are valid", (done) => {
			let Reservation = {
				Patient: {
					patientId: "625eebea609bd5749e2c5866",
					healthId: "string",
					name: "Dev",
					gender: "Male",
					dob: "2022-03-07T04:39:49.468Z",
					phone: "+910123456789",
				},
				Provider: {
					memberId: "new",
					name: "string",
				},
				reservationFor: "Doctor",
				reservationTime: "2022-04-06T08:54:59.175Z",
				status: "CheckedIn",
				tokenNumber: "1",
				label: "Emergency",
				audit: {
					dateCreated: "2022-04-20T05:59:09.898Z",
					createdBy: "string",
					dateModified: "2022-04-20T05:59:09.898Z",
					modifiedBy: "string",
				},
			};
			chai
				.request(app)
				.post("/reservations")
				.send(Reservation)
				.end((err, res) => {
					providerId = res.body.Provider.memberId;
					reservationId = res.body._id;
					status = res.body.status;
					res.should.have.status(200);
					res.body.should.be.a("object");
					done();
				});
		});
	});

	describe("/GET/filter fetchReservation", () => {
		it("it should not GET a reservation if the date is not given", (done) => {
			chai
				.request(app)
				.get("/reservations/filter?page=1")
				.end((err, res) => {
					res.should.have.status(206);
					res.body.should.be.a("object");
					res.body.should.have.property("errors");
					res.body.errors[0].should.have.property("field").eql("date");
					done();
				});
		});
		it("it should GET a reservation if the date and page is given", (done) => {
			chai
				.request(app)
				.get("/reservations/filter?page=1&date=2022-02-21")
				.end((err, res) => {
					res.should.have.status(200);
					res.body.should.be.a("object");
					res.body.should.have.property("meta");
					res.body.should.have.property("data");
					done();
				});
		});
		it("it should GET a reservation if the date, providerId and page is given", (done) => {
			chai
				.request(app)
				.get(
					`/reservations/filter?page=1&date=2022-02-21&providerId=${providerId}`
				)
				.end((err, res) => {
					res.should.have.status(200);
					res.body.should.be.a("object");
					res.body.should.have.property("meta");
					res.body.should.have.property("data");
					done();
				});
		});
		it("it should GET a reservation if the date, providerId, status and page is given", (done) => {
			chai
				.request(app)
				.get(
					`/reservations/filter?page=1&date=2022-02-21&providerId=${providerId}&status=${status}`
				)
				.end((err, res) => {
					res.should.have.status(200);
					res.body.should.be.a("object");
					res.body.should.have.property("meta");
					res.body.should.have.property("data");
					done();
				});
		});
		it("it should GET a reservation if the date, status and page is given", (done) => {
			chai
				.request(app)
				.get(`/reservations/filter?page=1&date=2022-02-21&status=${status}`)
				.end((err, res) => {
					res.should.have.status(200);
					res.body.should.be.a("object");
					res.body.should.have.property("meta");
					res.body.should.have.property("data");
					done();
				});
		});
		it("it should GET a reservation if the date and status is given", (done) => {
			chai
				.request(app)
				.get(`/reservations/filter?date=2022-02-21&status=${status}`)
				.end((err, res) => {
					res.should.have.status(200);
					res.body.should.be.a("object");
					res.body.should.have.property("meta");
					res.body.should.have.property("data");
					done();
				});
		});
	});

	describe("/GET/:reservationId filterReservation", () => {
		it("it should GET a reservation by the given id", (done) => {
			chai
				.request(app)
				.get(`/reservations/${reservationId}`)
				.end((err, res) => {
					res.should.have.status(200);
					res.body.should.be.a("object");
					res.body.should.have.property("_id").eql(reservationId);
					res.body.should.have.property("Patient");
					res.body.Patient.should.be.a("object");
					res.body.Patient.should.have.property("patientId");
					res.body.should.have.property("Provider");
					res.body.should.have.property("audit");
					done();
				});
		});
	});

	describe("/PATCH/:reservationId updateReservation", () => {
		it("it should fetch a reservation by the given id", (done) => {
			chai
				.request(app)
				.patch(`/reservations/${reservationId}`)
				.end((err, res) => {
					res.should.have.status(200);
					res.body.should.be.a("object");
					res.body.should.have.property("_id").eql(reservationId);
					res.body.should.have.property("Patient");
					res.body.Patient.should.be.a("object");
					res.body.should.have.property("audit");
					done();
				});
		});
		it("it should not fetch a reservation if the given id is invalid", (done) => {
			chai
				.request(app)
				.patch("/reservations/jgj")
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
				.patch(`/reservations/${reservationId}`)
				.end((err, res) => {
					res.should.have.status(200);
					res.body.should.be.a("object");
					res.body.should.have.property("_id").eql(reservationId);
					res.body.should.have.property("Patient");
					res.body.Patient.should.be.a("object");
					res.body.should.have.property("audit");
					done();
				});
		});
	});

	after((done) => {
		reservations.remove(() => {
			done();
		});
	});
});
