import unittest
import requests


class TestParkingAPI(unittest.TestCase):
    def setUp(self):
        self.base_url = "http://localhost:8080"
        self.sample_rates = {
            "rates": [
                {
                    "days": "mon,tues,thurs",
                    "times": "0900-2100",
                    "tz": "America/Chicago",
                    "price": 1500,
                },
                {
                    "days": "fri,sat,sun",
                    "times": "0900-2100",
                    "tz": "America/Chicago",
                    "price": 2000,
                },
                {
                    "days": "wed",
                    "times": "0600-1800",
                    "tz": "America/Chicago",
                    "price": 1750,
                },
                {
                    "days": "mon,wed,sat",
                    "times": "0100-0500",
                    "tz": "America/Chicago",
                    "price": 1000,
                },
                {
                    "days": "sun,tues",
                    "times": "0100-0700",
                    "tz": "America/Chicago",
                    "price": 925,
                },
            ]
        }

        response = requests.put(f"{self.base_url}/rates", json=self.sample_rates)
        self.assertEqual(response.status_code, 200, "Failed to load initial rates")

    def test_price_available(self):
        response = requests.get(
            f"{self.base_url}/price?start=2015-07-01T07:00:00-05:00&end=2015-07-01T12:00:00-05:00"
        )
        self.assertEqual(response.status_code, 200)
        price_data = response.json()
        self.assertEqual(
            price_data["price"], 1750, "Price does not match expected value"
        )

    def test_price_unavailable_multiple_days(self):
        response = requests.get(
            f"{self.base_url}/price?start=2015-07-01T07:00:00-05:00&end=2015-07-02T12:00:00-05:00"
        )
        self.assertEqual(response.status_code, 200)
        price_data = response.json()
        self.assertEqual(
            price_data["price"],
            "unavailable",
            "Expected 'unavailable' for multiple days",
        )

    def test_price_unavailable_multiple_rates(self):
        response = requests.get(
            f"{self.base_url}/price?start=2015-07-01T07:00:00-05:00&end=2015-07-01T19:00:00-05:00"
        )
        self.assertEqual(response.status_code, 200)
        price_data = response.json()
        self.assertEqual(
            price_data["price"],
            "unavailable",
            "Expected 'unavailable' for multiple rates",
        )

    def test_price_unavailable_no_matching_rate(self):
        response = requests.get(
            f"{self.base_url}/price?start=2015-07-01T00:00:00-05:00&end=2015-07-01T01:00:00-05:00",
        )
        self.assertEqual(response.status_code, 200)
        price_data = response.json()
        self.assertEqual(
            price_data["price"],
            "unavailable",
            "Expected 'unavailable' for no matching rate",
        )

    def test_update_rates(self):
        new_rates = {
            "rates": [
                {
                    "days": "mon",
                    "times": "0000-2359",
                    "tz": "America/New_York",
                    "price": 5000,
                }
            ]
        }
        response = requests.put(f"{self.base_url}/rates", json=new_rates)
        self.assertEqual(response.status_code, 200)
        response = requests.get(
            f"{self.base_url}/price?start=2024-08-05T00:00:00-04:00&end=2024-08-05T23:59:00-04:00",
        )
        self.assertEqual(response.status_code, 200)
        price_data = response.json()
        self.assertEqual(price_data["price"], 5000, "Price does not match updated rate")


if __name__ == "__main__":
    unittest.main()
