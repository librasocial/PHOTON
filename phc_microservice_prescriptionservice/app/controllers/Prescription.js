const { validationResult } = require("express-validator");
const ApiError = require("../errors/handler");
const PrescriptionModel = require("../models/PrescriptionModel");
const appLogger = require("../../config/logger.js");
const kafka = require("../middlewares/kafka");
const ObjectId = require("mongodb").ObjectID;

module.exports = {
  async create(req, res, next) {
    const errors = validationResult(req);
    let authHeader = req.headers["authorization"];
    let idToken = req.headers["idToken"];

    if (!errors.isEmpty()) {
      return next(ApiError.expressValidationFailed(errors.errors));
    }

    try {
      const prescription = new PrescriptionModel(req.body);

      prescription.careContext = new ObjectId().toString();

      prescription.audit = {
        dateCreated: Date.now(),
        createdBy: req.authorization.username,
        dateModified: Date.now(),
        modifiedBy: req.authorization.username,
      };

      const savedPrescription = await prescription.save();

      res.status(201).json(savedPrescription);
      kafka.produce({
        authHeader: authHeader,
        domainObject: savedPrescription,
        idToken: idToken,
        type: "PharmacyOrderCreated",
      });
    } catch (error) {
      res.status(500).json(error);
      appLogger.error("node error", error);
      next(error);
    }
  },
  async fetch(req, res, next) {
    const errors = validationResult(req);

    if (!errors.isEmpty()) {
      return next(ApiError.expressValidationFailed(errors.errors));
    }

    try {
      const prescriptionId = req.params.prescriptionId;

      if (!prescriptionId) {
        return next(
          ApiError.partialContent(
            "prescriptionId is required",
            "prescriptionId"
          )
        );
      } else {
        const prescription = await PrescriptionModel.findOne({
          _id: prescriptionId,
        });

        if (!prescription) {
          return next(
            ApiError.notFound(
              `Prescription not found for ${prescriptionId}`,
              "prescriptionId"
            )
          );
        } else {
          res.status(200).json(prescription);
        }
      }
    } catch (error) {
      next(error);
    }
  },
  async update(req, res, next) {
    const errors = validationResult(req);
    let authHeader = req.headers["authorization"];
    let idToken = req.headers["idToken"];

    if (!errors.isEmpty()) {
      return next(ApiError.expressValidationFailed(errors.errors));
    }

    try {
      const prescriptionId = req.params.prescriptionId;

      if (!prescriptionId) {
        return next(
          ApiError.partialContent(
            "prescriptionId is required",
            "prescriptionId"
          )
        );
      } else {
        const fieldsToUpdateArray = [
          "patientId",
          "UHId",
          "encounterId",
          "prescribedBy",
          "prescribedOn",
          "partOfPrescription",
          "medicalTests",
          "reviewAfter",
        ];

        let update = {};

        fieldsToUpdateArray.forEach((field) => {
          if (req.body[field]) {
            update[field] = req.body[field];
          }
        });

        if (req.body.products) {
          update.products = req.body.products;
        }

        if (req.body.medicalTests) {
          for (let i = 0; i < req.body.medicalTests.length; i++) {
            if (!update.medicalTests) {
              update.medicalTests = [];
            }

            update.medicalTests[i] = req.body.medicalTests[i];
          }
        }

        const prescription = await PrescriptionModel.findOneAndUpdate(
          { _id: prescriptionId },
          update,
          { new: true }
        );

        if (!prescription) {
          return next(
            ApiError.notFound(
              `Prescription not found for _id ${prescriptionId}`,
              "prescriptionId"
            )
          );
        } else {
          res.status(200).json(prescription);
          kafka.produce({
            authHeader: authHeader,
            domainObject: prescription,
            idToken: idToken,
            type: "PharmacyOrderupdated",
          });
        }
      }
    } catch (error) {
      appLogger.error("error", error);
      next(error);
    }
  },
  async filter(req, res, next) {
    const errors = validationResult(req);

    if (!errors.isEmpty()) {
      return next(ApiError.expressValidationFailed(errors.errors));
    }

    try {
      const {
        encounterDate,
        UHId,
        doctor,
        encounterId,
        abhaAddress,
        careContext,
        page,
        size,
      } = req.query;

      if (isNaN(+size)) {
        next(ApiError.partialContent("No or invalid value found", "size"));
        return;
      }
      if (isNaN(+page)) {
        next(ApiError.partialContent("No or invalid value found", "page"));
        return;
      }

      const skip = (page - 1) * size;

      const filter = {};
      const respo = {};

      if (encounterDate) {
        let givenDate = new Date(encounterDate);
        givenDate.setDate(givenDate.getDate() + 1);
        filter.encounterDate = {
          $gte: new Date(encounterDate).toISOString(),
          $lt: givenDate.toISOString(),
        };
      }
      if (UHId) filter.UHId = UHId;
      if (doctor) filter.prescribedBy = doctor;
      if (encounterId) filter.encounterId = encounterId;
      if (careContext) filter.careContext = careContext;
      if (abhaAddress) filter.abhaAddress = abhaAddress;

      const prescription = await PrescriptionModel.find(filter)
        .limit(+size)
        .skip(skip);

      const count = await PrescriptionModel.countDocuments(filter);

      if (prescription !== null) {
        const meta = {
          totalPages: +page ? Math.ceil(count / size) : 1,
          totalElements: count,
          number: +page,
          size: +size,
        };

        respo.meta = meta;
        respo.data = prescription;

        res.status(200).json(respo);
      } else {
        next(ApiError.notFound("No Prescription found"));
      }
    } catch (error) {
      console.log(error)
      res.status(500).json(error);
    }
  },
};
