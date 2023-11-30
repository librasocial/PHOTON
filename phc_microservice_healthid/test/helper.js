const fs = require('fs').promises;

function loadData(filePath) {
  return new Promise(function (resolve, reject) {
    fs.readFile(filePath).then((data) => {
      const a = JSON.parse(data.toString())
      resolve(a);
    });
  })
}

module.exports = { loadData }
