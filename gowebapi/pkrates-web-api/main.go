package main

import (
	"encoding/json"
	"fmt"
	"io"
	"net/http"
	"os"
	con "strconv"
	s "strings"
	"sync"
	"time"

	"github.com/gin-gonic/gin"
)

// A Rate is an object for encapsulating parking rate data
type Rate struct {
	Days     string `json:"days"`
	Times    string `json:"times"`
	TimeZone string `json:"tz"`
	Price    int    `json:"price"`
} //end struct

// A RateModel is an object for encapsulating a slice of Rates
type RateModel struct {
	RateModel []Rate `json:"rates"`
} //end struct

// A Price is an object for encapsulating a parking price
type Price struct {
	Price int `json:"price"`
} //end struct

// A UnavailablePrice is an object for encapsulating price message
type UnavailablePrice struct {
	Price string `json:"price"`
}

// Global variable used by application api
var (
	rates RateModel
	mu    sync.RWMutex
)

// Main used to start server and host api endpoints for parking rates
func main() {
	//load json file
	loadJsonFile()

	router := gin.Default()
	router.GET("/rates", getRates)
	router.PUT("/rates", putRates)
	router.GET("/price", getPrice)

	router.Run("localhost:8080")
} //end func

// Preloads JSON file located within same directory as server
func loadJsonFile() {
	ratesJson, err := os.Open("Rates.json")

	if err != nil {
		fmt.Println(err)
	} //end if
	fmt.Println("Opened rates.json")

	//defer will make sure the file is closed upon completion of the main func
	defer ratesJson.Close()

	fmt.Println("Read json file into byte array")
	byteArray, _ := io.ReadAll(ratesJson)

	fmt.Println("Load rates slice.")
	json.Unmarshal(byteArray, &rates)
} //end func

// Webservice endpoint /rates (GET) that responds with a list of all rates as JSON.
func getRates(c *gin.Context) {
	mu.RLock()
	c.IndentedJSON(http.StatusOK, rates)
	mu.RUnlock()
} //end function

// Webservice endpoint /rates (PUT) that saves the rates the user supplied as JSON
func putRates(c *gin.Context) {
	var model RateModel

	// BindJSON binds the sent JSON to RateModel.
	if err := c.BindJSON(&model); err != nil {
		fmt.Println(err)
		return
	} //end if
	mu.Lock()
	rates.RateModel = nil                                         //empty it then
	rates.RateModel = append(rates.RateModel, model.RateModel...) //fill it back up
	mu.Unlock()
	c.Status(http.StatusOK)
} //end func

// Webservice endpoint /price (GET) that gets a parking price based off of the given user input of date/time ranges
func getPrice(c *gin.Context) {
	var price Price
	start := c.Query("start")
	end := c.Query("end")

	startDtTime, stErr := time.Parse(time.RFC3339, start)
	endDtTime, endErr := time.Parse(time.RFC3339, end)
	if stErr != nil || endErr != nil {
		fmt.Println("Could not parse users input time range:", stErr, endErr)
		c.Status(http.StatusBadRequest)
		return
	} //end if

	//(more than one day?)
	if endDtTime.Sub(startDtTime).Hours() >= 24 || endDtTime.Sub(startDtTime).Hours() < 0 {
		fmt.Println("more than one day or incorrect date span unavailable")
		c.IndentedJSON(http.StatusOK, UnavailablePrice{Price: "unavailable"})
		return
	} //end if

	//step 2 get list possible rates to check dates from
	lowerCaseDay := s.ToLower(startDtTime.Weekday().String())
	ratesFound := findRatesByDay(lowerCaseDay)
	processed := false
	for _, rate := range ratesFound { //loop over rates
		//parse time ranges; hours/minutes
		timesRanges := s.Split(rate.Times, "-") //split ranges
		rateStartHours, _ := con.Atoi(timesRanges[0][0:2])
		rateStartMinutes, _ := con.Atoi(timesRanges[0][2:4])
		rateEndHours, _ := con.Atoi(timesRanges[1][0:2])
		rateEndMinutes, _ := con.Atoi(timesRanges[1][2:4])

		//set rate start/end dates
		loc, _ := time.LoadLocation(rate.TimeZone)
		rateStartDtTm := time.Date(startDtTime.Year(), startDtTime.Month(), startDtTime.Day(), rateStartHours, rateStartMinutes, 0, 0, loc)
		rateEndDtTm := time.Date(startDtTime.Year(), startDtTime.Month(), startDtTime.Day(), rateEndHours, rateEndMinutes, 0, 0, loc)

		if (startDtTime.After(rateStartDtTm) || startDtTime.Equal(rateStartDtTm)) && startDtTime.Before(rateEndDtTm) && endDtTime.After(rateStartDtTm) && (endDtTime.Before(rateEndDtTm) || endDtTime.Equal(rateEndDtTm)) {
			if processed {
				fmt.Println("multiple rates selected to be processed")
				c.IndentedJSON(http.StatusOK, UnavailablePrice{Price: "unavailable"})
				return
			} //end if
			price = Price{Price: rate.Price}
			processed = true
		} //end if
	} //end for

	if price.Price == 0 {
		fmt.Println("no rate found for time range")
		c.IndentedJSON(http.StatusOK, UnavailablePrice{Price: "unavailable"})
	} else {
		c.IndentedJSON(http.StatusOK, price)
	} //end if...else
} //end func

// Filters for rates based off a day of the week
func findRatesByDay(dayOfTheWeek string) []Rate {
	var foundRates []Rate
	mu.RLock()
	for _, rate := range rates.RateModel { //loop over rates
		for _, day := range s.Split(rate.Days, ",") { //need to split days
			if s.Contains(dayOfTheWeek, s.Trim(day, " ")) { //add the rate to slice
				foundRates = append(foundRates, rate)
			} //end if
		} //end for
	} //end for
	mu.RUnlock()
	return foundRates
} //end func
