#!/usr/bin/env python

import unittest
import sys
from mock import MagicMock

from tire_pressure_monitoring import Alarm, Sensor

LOW_PRESSURE_THRESHOLD = 17.0
HIGH_PRESSURE_THRESHOLD = 21.0

# See http://docs.oracle.com/cd/E19957-01/806-3568/ncg_goldberg.html
EPSILON = HIGH_PRESSURE_THRESHOLD * sys.float_info.epsilon

class AlarmTest(unittest.TestCase):

  def create_sensor(self, next_psi_value):
    sensor = MagicMock(spec=Sensor)
    sensor.pop_next_pressure_psi_value.return_value = next_psi_value
    return sensor

  def create_alarm(self, next_psi_value = LOW_PRESSURE_THRESHOLD):
    alarm = Alarm(
        self.create_sensor(next_psi_value),
        LOW_PRESSURE_THRESHOLD,
        HIGH_PRESSURE_THRESHOLD)
    return alarm

  def create_checked_alarm(self, next_psi_value = LOW_PRESSURE_THRESHOLD):
    alarm = self.create_alarm(next_psi_value)
    alarm.check()
    return alarm

  def test_default_alarm_state(self):
    alarm = self.create_alarm()
    self.assertFalse(alarm.is_alarm_on)

  def test_low_pressure_threshold(self):
    alarm = self.create_checked_alarm(LOW_PRESSURE_THRESHOLD)
    self.assertFalse(alarm.is_alarm_on)

  def test_high_pressure_threshold(self):
    alarm = self.create_checked_alarm(HIGH_PRESSURE_THRESHOLD)
    self.assertFalse(alarm.is_alarm_on)

  def test_less_than_low_pressure_threshold(self):
    alarm = self.create_checked_alarm(
      LOW_PRESSURE_THRESHOLD - EPSILON)
    self.assertTrue(alarm.is_alarm_on)

  def test_greater_than_low_pressure_threshold(self):
    alarm = self.create_checked_alarm(
      HIGH_PRESSURE_THRESHOLD + EPSILON)
    self.assertTrue(alarm.is_alarm_on)

if __name__ == "__main__":
  unittest.main()
