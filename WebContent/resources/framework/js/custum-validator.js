/* ------------------------ Validate date ----------------------------- */
	/**
	 * Validate date
	 * @param sDate
	 * @returns {Boolean}
	 */
	function isValidDate(sDate){
		if(sDate){
			var sSeparator = '/';
			if(!sDate.match("^[0-9]{2}/[0-9]{2}/[0-9]{4}$")) return false;
			var arDate = sDate.split(sSeparator);
			// --------> Pour courriger le bug du parseInt("09") utiliser soit parseFloat ou parseInt("09", 10) : base 10
			var iDay = parseInt(arDate[0], 10);
			var iMonth = parseInt(arDate[1], 10);
			var iYear = parseInt(arDate[2]);
			var arDayPerMonth = [31,(isLeapYear(iYear))?29:28,31,30,31,30,31,31,30,31,30,31];
			//
			if(!arDayPerMonth[iMonth-1]){
				return false;
			}

			return (iDay <= arDayPerMonth[iMonth-1] && iDay > 0);
		}

		return true;
	}

	/**
	 * Validate date hour
	 * @param sDateHour
	 * @returns {Boolean}
	 */
	function isValidDateHour(sDateHour){
		var sSeparator = ' ';
		var arDateHour = sDateHour.split(sSeparator);
		return (arDateHour[0] && arDateHour[1] && isValidDate(arDateHour[0]) && isHour(arDateHour[1]));
	}


	/**
	 * @param sHour
	 * @returns {Boolean}
	 */
	function isHour(sHour){
		var sSeparator = ':';
		var withSeconds = false;
		var iSecs = 0;
		//
		if(sHour.match("^[0-9]{2}:[0-9]{2}:[0-9]{2}$")){
			withSeconds = true;
		} else if(!sHour.match("^[0-9]{2}:[0-9]{2}$")){
			return false;
		}

		var arHour = sHour.split(sSeparator);
		var iHour = parseInt(arHour[0]);
		var iMinute = parseInt(arHour[1]);
		//
		if(withSeconds){
			iSecs = parseInt(arHour[2]);
		} else{
			iSecs = 0;
		}

		return 	(iHour >= 0 && iHour < 24) && (iMinute >= 0 && iMinute < 60) && (iSecs >= 0 && iSecs < 60);
	}

	/**
	 * @param iYear
	 * @returns {Boolean}
	 */
	function isLeapYear(iYear){
		return ((iYear%4==0 && iYear%100!=0) || iYear%400==0);
	}
	/* ------------------------ Fin Validate date ----------------------------- */