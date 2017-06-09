import random

class Sensor(object):
    # The reading of the pressure value from the sensor is simulated in this implementation.
    # Because the focus of the exercise is on the other class.
    _OFFSET = 16

    def pop_next_pressure_psi_value(self):
        pressure_telemetry_value = self.sample_pressure()
        return Sensor._OFFSET + pressure_telemetry_value

    @staticmethod
    def sample_pressure():
        # placeholder implementation that simulate a real sensor in a real tire
        pressure_telemetry_value = 6 * random.random() * random.random()
        return float(pressure_telemetry_value)


class Alarm(object):

    _LOW_PRESSURE_THRESHOLD = 17.0
    _HIGH_PRESSURE_THRESHOLD = 21.0

    def __init__(self,
                 sensor = Sensor(),
                 low_pressure_threshold = _LOW_PRESSURE_THRESHOLD,
                 high_pressure_threshold = _HIGH_PRESSURE_THRESHOLD):
        self._low_pressure_threshold = low_pressure_threshold
        self._high_pressure_threshold = high_pressure_threshold
        self._sensor = sensor
        self._is_alarm_on = False

    def check(self):
        psi_pressure_value = self._sensor.pop_next_pressure_psi_value()
        if psi_pressure_value < self._low_pressure_threshold or psi_pressure_value > self._high_pressure_threshold:
            self._is_alarm_on = True

    @property
    def is_alarm_on(self):
        return self._is_alarm_on
