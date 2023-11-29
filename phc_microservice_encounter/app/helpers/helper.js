const assertNotNull = function assertNotNull(object, key) {
    if (object === null || object === undefined) {
        throw new Error(JSON.stringify({ msg: `${key} is asserted to be non null`, field: key }))
    } else {
        return object
    }
}

module.exports = { assertNotNull }
