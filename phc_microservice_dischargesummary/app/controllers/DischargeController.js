const { validationResult } = require('express-validator')
const ApiError = require('../errors/handler')
const DischargeSummaryModel = require('../models/DischargeModel')

module.exports = {
  async create (req, res, next) {
    const errors = validationResult(req)

    if (!errors.isEmpty()) {
      return next(ApiError.expressValidationFailed(errors.errors))
    }

    try {
      const receivedData = new DischargeSummaryModel(req.body)

      receivedData.audit = {
        dateCreated: Date.now(),
        createdBy: req.authorization.username,
        dateModified: Date.now(),
        modifiedBy: req.authorization.username
      }

      const newData = await receivedData.save()

      res.status(201).json(newData)
    } catch (error) {
      next(error)
    }
  },
  async fetch (req, res, next) {
    const errors = validationResult(req)

    if (!errors.isEmpty()) {
      return next(ApiError.expressValidationFailed(errors.errors))
    }

    try {
      const { historyId } = req.params

      if (!historyId) {
        return next(
          ApiError.partialContent('historyId is required', 'historyId')
        )
      } else {
        const data = await DischargeSummaryModel.findById(historyId)

        if (!data) {
          return next(
            ApiError.notFound(`Discharge Summary not found for ${historyId}`, 'historyId')
          )
        } else {
          res.status(200).json(data)
        }
      }
    } catch (error) {
      next(error)
    }
  },
  async update (req, res, next) {
    const errors = validationResult(req)

    if (!errors.isEmpty()) {
      return next(ApiError.expressValidationFailed(errors.errors))
    }

    try {
      const { historyId } = req.params

      if (!historyId) {
        return next(
          ApiError.partialContent('historyId is required', 'historyId')
        )
      } else {
        const updatedData = await DischargeSummaryModel.findByIdAndUpdate(
          historyId,
          req.body,
          { new: true }
        )

        if (!updatedData) {
          return next(
            ApiError.notFound(`Discharge Summary not found for ${historyId}`, 'historyId')
          )
        } else {
          res.status(200).json(updatedData)
        }
      }
    } catch (error) {
      next(error)
    }
  },
  async filter (req, res, next) {
    const errors = validationResult(req)

    if (!errors.isEmpty()) {
      return next(ApiError.expressValidationFailed(errors.errors))
    }

    try {
      const { encounterDate, UHId, doctor, encounterId, type, page, size } = req.query

      if (isNaN(+size)) {
        next(ApiError.partialContent('No or invalid value found', 'size'))
        return
      }
      if (isNaN(+page)) {
        next(ApiError.partialContent('No or invalid value found', 'page'))
        return
      }

      const skip = (page - 1) * size

      const filter = {}
      const respo = {}

      if (encounterDate) {
        const givenDate = new Date(encounterDate)
        givenDate.setDate(givenDate.getDate() + 1)
        filter.encounterDate = {
          $gte: new Date(encounterDate).toISOString(),
          $lt: givenDate.toISOString()
        }
      }
      if (UHId) filter.UHId = UHId
      if (doctor) filter.doctor = doctor
      if (encounterId) filter.encounterId = encounterId
      if (type) filter.type = type

      const DischargeSummaryData = await DischargeSummaryModel.find(filter)
        .limit(+size)
        .skip(skip)

      const count = await DischargeSummaryModel.countDocuments(filter)

      if (DischargeSummaryData !== null) {
        const meta = {
          totalPages: +page ? Math.ceil(count / size) : 1,
          totalElements: count,
          number: +page,
          size: +size
        }

        respo.meta = meta
        respo.data = DischargeSummaryData

        res.status(200).json(respo)
      } else {
        next(ApiError.notFound('No Data found for given parameters'))
      }
    } catch (error) {
      next(error)
    }
  }
}
