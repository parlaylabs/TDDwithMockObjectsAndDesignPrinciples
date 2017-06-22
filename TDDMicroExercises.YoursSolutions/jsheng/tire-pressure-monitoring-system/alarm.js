const Sensor = require('./sensor');

var alarm = false;
var sensor = new Sensor();
var thresholds = {
  lowPressureThreshold: 17,
  highPressureThreshold: 21
};

Alarm = function() {
	this.currentValue = (thresholds.highPressureThreshold + thresholds.lowPressureThreshold)/2;
};

Alarm.prototype = {

  checkAlarm: function () {
    if (this.currentValue < thresholds.lowPressureThreshold ||
      thresholds.highPressureThreshold < this.currentValue)
    {
      alarm = true;
    } else {
      alarm = false;
    }
    return alarm;
  },

	getNextPressureReading: function () {
		this.currentValue = sensor.popNextPressurePsiValue();
		return this.currentValue;
	},

	getThresholds: function() {
		return thresholds;
	},

	setThresholds: function(lowThreshold, highThreshold) {
  	if (lowThreshold >= highThreshold) {
  		throw 'High Threshold must be larger than Low Threshold';
		}
  	thresholds.lowPressureThreshold = lowThreshold;
  	thresholds.highPressureThreshold = highThreshold;
	}
};

module.exports = Alarm;