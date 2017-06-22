const Alarm = require('../tire-pressure-monitoring-system/alarm');

const assert = require('chai').assert;
const sinon = require('sinon');

describe('Tire Pressure Monitoring System', function () {
  this.timeout(5000);
	describe('testing the alarm', function () {
    let testAlarm, thresholds;

    beforeEach(function () {
      testAlarm = new Alarm();
      thresholds = testAlarm.getThresholds();
    });

    it('validate alarm is not on during initialization', function () {
      assert.isFalse(testAlarm.checkAlarm());
    });

    it('validate starting threshold low', function () {
      assert.equal(thresholds.lowPressureThreshold, 17);
    });

    it('validate starting threshold high', function () {
      assert.equal(thresholds.highPressureThreshold, 21);
    });

    it('validate setting threshold low', function () {
      testAlarm.setThresholds(15, thresholds.highPressureThreshold);
      const newThreshold = testAlarm.getThresholds();
      assert.equal(newThreshold.lowPressureThreshold, 15);
      assert.equal(newThreshold.highPressureThreshold, thresholds.highPressureThreshold);
    });

    it('validate setting threshold high', function () {
      testAlarm.setThresholds(thresholds.lowPressureThreshold, 18);
      const newThreshold = testAlarm.getThresholds();
      assert.equal(newThreshold.highPressureThreshold, 18);
      assert.equal(newThreshold.lowPressureThreshold, thresholds.lowPressureThreshold);
    });

    it('validate High Threshold must be larger than Low Threshold', function () {
      assert.throws(
        function () {
          testAlarm.setThresholds(15, 12)
        }, 'High Threshold must be larger than Low Threshold');
    });

    it('validate alarm goes off when it hits low threshold', function () {
      testAlarm.setThresholds(25, 30);
      testAlarm.getNextPressureReading();
      assert.isTrue(testAlarm.checkAlarm());
    });

    it('validate alarm goes off when it hits high threshold', function () {
      testAlarm.setThresholds(0, 5);
      testAlarm.getNextPressureReading();
      assert.isTrue(testAlarm.checkAlarm());
    });

    it('validate alarm on then off after changing threshold', function () {
      testAlarm.setThresholds(0, 5);
      testAlarm.getNextPressureReading();
      assert.isTrue(testAlarm.checkAlarm());
      testAlarm.setThresholds(0, 100);
      assert.isFalse(testAlarm.checkAlarm());
    });
  });
});

