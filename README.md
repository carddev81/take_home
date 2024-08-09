# **Unlocked Labs Programming Take-Home Interview Repository**

---

## The Application

Build an API that allows a user to enter a date time range and get back the price at which they would be charged to park for that time span

### API Endpoints

- The application publishes an API with two endpoints that computes a price for an input datetime range.
- The API must respond to requests on port `8080`
- The API must respond using JSON

#### Rates

The first endpoint is `rates`.

This path takes a `PUT` where rate information can be updated by submitting a modified rates JSON. This submitted JSON overwrites the stored rates.

- A rate is comprised of a price, time range the rate is valid, and days of the week the rate applies to
- See the section `Sample JSON for testing` for the initial set of seeded rates. These rates are expected to be loaded into the database at application startup.

This path when requested with a `GET` returns the rates stored.

#### Price

The second endpoint is `price`. It allows the user to request the price for a requested time.

- It uses query parameters for requesting the price
- The user specifies input date/times as ISO-8601 with timezones
- The paramters are `start` and `end`.
- An example query is `?start=2015-07-01T07:00:00-05:00&end=2015-07-01T12:00:00-05:00`
- Response is an object containing the field `price`

  - ```
    {
       "price": 5000
    }
    ```

##### Response Requirements

- User input can span more than one day, but the API mustn't return a valid price - it must return `"unavailable"`
- User input can span multiple rates, but the API mustn't return a valid price - it must return `"unavailable"`
- Rates will not span multiple days

### Application startup

The rates are specified by a file read into memory on start of the application, either from a specific location
or perhaps an environment variable that can specify the file path.

## Other Optional Requirements

- Documentation that one can follow to build and run the application
- Documentation of the API endpoints
- Unit/Integration Tests

## Sample JSON for testing

```json
{
  "rates": [
    {
      "days": "mon,tues,thurs",
      "times": "0900-2100",
      "tz": "America/Chicago",
      "price": 1500
    },
    {
      "days": "fri,sat,sun",
      "times": "0900-2100",
      "tz": "America/Chicago",
      "price": 2000
    },
    {
      "days": "wed",
      "times": "0600-1800",
      "tz": "America/Chicago",
      "price": 1750
    },
    {
      "days": "mon,wed,sat",
      "times": "0100-0500",
      "tz": "America/Chicago",
      "price": 1000
    },
    {
      "days": "sun,tues",
      "times": "0100-0700",
      "tz": "America/Chicago",
      "price": 925
    }
  ]
}
```

The timezones specified in the JSON file adhere to the current version of the tz database. Assume that there could be other (non America/Chicago) timezones specified. For more information: <https://en.wikipedia.org/wiki/List_of_tz_database_time_zones>

Assume that rates in this file will never overlap

## Sample result

Datetime ranges must be specified in ISO-8601 format. A rate must completely encapsulate a datetime range for it to be available.

- `2015-07-01T07:00:00-05:00` to `2015-07-01T12:00:00-05:00` must yield `{'price': 1750}`
- `2015-07-04T15:00:00+00:00` to `2015-07-04T20:00:00+00:00` must yield `{'price': 2000}`
- `2015-07-04T07:00:00+05:00` to `2015-07-04T20:00:00+05:00` must yield `"unavailable"`

## Submitting

Please commit your code to your fork of this repository, using the [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/#specification)` format,
separated into orderly and logical commits.

There will be github actions with automated tests to send requests against your submission, and to ensure conventional commits are used.

We will be reviewing and walking through your code in your next interview to discuss the implementation, your design choices and how you approached the problem.
