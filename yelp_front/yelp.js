function initialize() 
{
    var mapCanvas = document.getElementById('map-canvas');
    var mapOptions = 
    {
        center: new google.maps.LatLng(44.5403, -78.5463),
        zoom: 8,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    }
    var map = new google.maps.Map(mapCanvas, mapOptions)
}
google.maps.event.addDomListener(window, 'load', initialize);

var names = [];
var longi= [];
var lat = [];
var rating = [];
var city = [];
function get_data()
{
	var i;
	id = "";
	var val = document.getElementById('user_id');
	id = val.value;
	if(typeof id === 'undefined' || id == ""){
   		alert("Please enter a user id");
   		window.location = "home.html";
 	};
	
	var url = "http://yelpbusinessreco-env.elasticbeanstalk.com/RequestHandlerServlet?uid="+id+"&numBusinesses=20";
	//var data = [{"name":"Rocket Burger & Subs","predictedRating":"3.9860177","state":"AZ","longitude":"-112.134448","latitude":"33.595874","bid":"F8q_9PUl-Lwjj9xIRPArcg","city":"Phoenix"},{"name":"Bobby Q","predictedRating":"3.8923705","state":"AZ","longitude":"-112.116198","latitude":"33.560853","bid":"rZbHg4ACfN3iShdsT47WKQ","city":"Phoenix"},{"name":"FEZ","predictedRating":"3.8660386","state":"AZ","longitude":"-112.0734977","latitude":"33.4916953","bid":"EWMwV5V9BxNs_U6nNVMeqw","city":"Phoenix"},{"name":"Pappadeaux Seafood Kitchen","predictedRating":"3.8603396","state":"AZ","longitude":"-112.115899","latitude":"33.587766","bid":"PmPOuRvuN3CoNOi1nBj_TQ","city":"Phoenix"},{"name":"Cabin Coffee Cafe","predictedRating":"3.8598828","state":"AZ","longitude":"-112.2000341","latitude":"33.71104","bid":"z2YTaHtGod3i3BBbKiMNwQ","city":"Glendale"},{"name":"Harkins Norterra 14","predictedRating":"3.8587618","state":"AZ","longitude":"-112.1136949","latitude":"33.7129714","bid":"m2hNiVSnMvXVRLMtsWUG_Q","city":"Phoenix"},{"name":"Discount Tire","predictedRating":"3.832917","state":"AZ","longitude":"-112.1881412","latitude":"33.6666355","bid":"K3_ahZoi_nzOIy8nfBKexg","city":"Glendale"},{"name":"Chick-fil-A","predictedRating":"3.8210146","state":"AZ","longitude":"-112.122882","latitude":"33.667746","bid":"ihMUU9D2mcgrp7fi8lmqJg","city":"Phoenix"},{"name":"Epic Carpet & Tile Care","predictedRating":"3.8182983","state":"AZ","longitude":"-112.0714835","latitude":"33.7635354","bid":"4HX6BEY8wsVZKGrd1Os8QA","city":"Phoenix"},{"name":"Pork on a Fork","predictedRating":"3.8104198","state":"AZ","longitude":"-112.092428","latitude":"33.683323","bid":"yYbd9P1KmlPSKmQxo68n_g","city":"Phoenix"},{"name":"Sweet Republic","predictedRating":"3.7986915","state":"AZ","longitude":"-111.883526","latitude":"33.58348","bid":"Bc4DoKgrKCtCuN-0O5He3A","city":"Scottsdale"},{"name":"Sonora Mesquite Grill","predictedRating":"3.797554","state":"AZ","longitude":"-111.9825532","latitude":"33.480203","bid":"KGX7O-_WqOIy9o7u9NOa9A","city":"Phoenix"},{"name":"Tacos Atoyac","predictedRating":"3.7974691","state":"AZ","longitude":"-112.0982917","latitude":"33.5386352","bid":"wN_wAXWg8W94v04eqijy6g","city":"Phoenix"},{"name":"Bamboo Cafe","predictedRating":"3.785242","state":"AZ","longitude":"-112.2020703","latitude":"33.7121163","bid":"_hTnLGWujfiZhIaCwWH8Ng","city":"Glendale"},{"name":"Postino Arcadia","predictedRating":"3.7769182","state":"AZ","longitude":"-111.996292","latitude":"33.502191","bid":"SDwYQ6eSu1htn8vHWv128g","city":"Phoenix"},{"name":"Cibo","predictedRating":"3.7754261","state":"AZ","longitude":"-112.079908","latitude":"33.45496","bid":"V1nEpIRmEa1768oj_tuxeQ","city":"Phoenix"},{"name":"QT 421","predictedRating":"3.7748482","state":"AZ","longitude":"-112.0998548","latitude":"33.6675872","bid":"fkoiZ4YDyIe8Fdm4tNBNGg","city":"Phoenix"},{"name":"Wandering Horse Buffet","predictedRating":"3.7725468","state":"AZ","longitude":"-111.869723796844","latitude":"33.5409028386777","bid":"Vn8qcaX64fkl8vByCsSvIg","city":"Scottsdale"},{"name":"Portillo's Hot Dogs","predictedRating":"3.771098","state":"AZ","longitude":"-111.888375410849","latitude":"33.5819443939844","bid":"5fu41tfqTU_LONb95t1Dkg","city":"Scottsdale"},{"name":"Scorpion Bay Marina","predictedRating":"3.7648606","state":"AZ","longitude":"-112.292289733886","latitude":"33.8708431412791","bid":"sCQ6qgzt4RnRnYXDgq50Uw","city":"Morristown"},{"name":"Mu Shu Asian Grill","predictedRating":"3.7561572","state":"AZ","longitude":"-112.09158","latitude":"33.480771","bid":"enbTOcl7WNgsjsAtmhvDRA","city":"Phoenix"},{"name":"Chino Bandido","predictedRating":"3.7557092","state":"AZ","longitude":"-112.100731","latitude":"33.626933","bid":"Zx8_4zKdDBSO3qGrkukBIA","city":"Phoenix"},{"name":"The Stock Shop","predictedRating":"3.7523866","state":"AZ","longitude":"-112.201332","latitude":"33.6105841","bid":"p8h-dXMTUMGKmPeQPN2rgQ","city":"Glendale"},{"name":"La Grande Orange Grocery","predictedRating":"3.7521796","state":"AZ","longitude":"-111.99575216742","latitude":"33.502312625663","bid":"R8VwdLyvsp9iybNqRvm94g","city":"Phoenix"},{"name":"Arizona Humane Society","predictedRating":"3.7507174","state":"AZ","longitude":"-112.089546","latitude":"33.570884","bid":"Snek2CGtaYW5NjjrTFT7Qw","city":"Phoenix"},{"name":"Matt's Big Breakfast","predictedRating":"3.7502701","state":"AZ","longitude":"-112.072327136993","latitude":"33.4566960573784","bid":"ntN85eu27C04nwyPa8IHtw","city":"Phoenix"},{"name":"Arizona Renaissance Festival","predictedRating":"3.7493188","state":"AZ","longitude":"-111.43694859314","latitude":"33.3257813896289","bid":"pgz6IeaZgLuzqrk6HMPhzA","city":"Gold Canyon"},{"name":"Sweet Life Gelateria","predictedRating":"3.74856","state":"AZ","longitude":"-111.9300279","latitude":"33.6556844","bid":"dP3bRr_N7Z8SkHNkZBXonw","city":"Phoenix"},{"name":"Lux","predictedRating":"3.7477815","state":"AZ","longitude":"-112.0741143","latitude":"33.5005719","bid":"aRkYtXfmEKYG-eTDf_qUsw","city":"Phoenix"},{"name":"Total Wine","predictedRating":"3.747289","state":"AZ","longitude":"-112.221227","latitude":"33.643428","bid":"RCc01aDaUvQ-sWfoalfjXA","city":"Glendale"},{"name":"Let's DO Dinner","predictedRating":"3.7445865","state":"AZ","longitude":"-112.2524822","latitude":"33.652635","bid":"guH4DGRw729jqhAeIzOsMw","city":"Peoria"},{"name":"DCR Titles","predictedRating":"3.7412946","state":"AZ","longitude":"-112.0840622","latitude":"33.6833064","bid":"6vqBtAA2RgXI5DfKCfH45g","city":"Phoenix"},{"name":"New China Super Buffet","predictedRating":"3.7395713","state":"AZ","longitude":"-112.081364","latitude":"33.639663","bid":"kCjR5nLyphuNKnDqRjurXQ","city":"Phoenix"},{"name":"Franks A Lot","predictedRating":"3.7375872","state":"AZ","longitude":"-111.9953819","latitude":"33.4483495","bid":"Qb0n61e_lsMmFuwwktjmjw","city":"Phoenix"},{"name":"Dutch Bros Coffee","predictedRating":"3.736095","state":"AZ","longitude":"-112.239527","latitude":"33.61008","bid":"5iYdwupq9xiko0PCUAY6hg","city":"Peoria"},{"name":"Cracker Barrel Old Country Store","predictedRating":"3.7356756","state":"AZ","longitude":"-112.114228","latitude":"33.681686","bid":"jqbrMBRsByyVBm8OoQ9dWA","city":"Phoenix"},{"name":"Los Reyes De La Torta","predictedRating":"3.7345488","state":"AZ","longitude":"-112.0655987","latitude":"33.5708369","bid":"bzDs0u8I-z231QVdIQWkrA","city":"Phoenix"},{"name":"AZ Mobile Massage","predictedRating":"3.7341244","state":"AZ","longitude":"-112.2026347","latitude":"33.664516","bid":"hXnpdtoFdZhRPaVaRravhQ","city":"Glendale"},{"name":"Discount Tire Store - Peoria","predictedRating":"3.7327113","state":"AZ","longitude":"-112.2679804","latitude":"33.7106075","bid":"1_qBaoEcCZrl2tIzNdRAkQ","city":"Peoria"},{"name":"Ray's Pizza","predictedRating":"3.7316787","state":"AZ","longitude":"-112.239905","latitude":"33.68106","bid":"IRUsIQSgjkk7Ah5RBsIfJg","city":"Peoria"},{"name":"Peoria Sports Complex","predictedRating":"3.731294","state":"AZ","longitude":"-112.2347564","latitude":"33.6312007","bid":"oajNY_oTIMb7YZsor7Xonw","city":"Peoria"},{"name":"Spinato's Pizza","predictedRating":"3.7312171","state":"AZ","longitude":"-112.0474847","latitude":"33.6405889","bid":"pF7uRzygyZsltbmVpjIyvw","city":"Phoenix"},{"name":"Cigar King","predictedRating":"3.730417","state":"AZ","longitude":"-111.9116185","latitude":"33.6154866","bid":"IwDI3Ls5FH2FxPNRyYaffA","city":"Scottsdale"},{"name":"Fresh Wasabi","predictedRating":"3.730122","state":"AZ","longitude":"-112.19974","latitude":"33.637161","bid":"0qUesn1TBPpPjW20h5Lqfg","city":"Glendale"},{"name":"Villa Deli","predictedRating":"3.7282977","state":"AZ","longitude":"-112.134346","latitude":"33.62758","bid":"4Vai2JNI48yffzQ7CMezBA","city":"Phoenix"},{"name":"Apple Store","predictedRating":"3.7279344","state":"AZ","longitude":"-112.2245556","latitude":"33.6397913","bid":"60XhkXuS1g9Aza2OPvMgIg","city":"Glendale"},{"name":"Pho Viet Vietnamese Restaurant","predictedRating":"3.7244322","state":"AZ","longitude":"-112.1498222","latitude":"33.6400698","bid":"NFMlj1TMVqzvo8jlQp0dUA","city":"Glendale"},{"name":"Starbucks Corporation","predictedRating":"3.7240102","state":"AZ","longitude":"-112.141960144043","latitude":"33.7132759094238","bid":"-CYr479M7Y49UqIPwFcjEg","city":"Glendale"},{"name":"Pita Jungle","predictedRating":"3.7238584","state":"AZ","longitude":"-111.9883722","latitude":"33.4954826","bid":"w19cemjVR8u02PgjFpJ7Mw","city":"Phoenix"},{"name":"The Shops at Norterra","predictedRating":"3.7227862","state":"AZ","longitude":"-112.114298","latitude":"33.715184","bid":"W4lcMQudkcx7sTabnWaOqw","city":"Phoenix"}]
	$.ajax({
      url:url,
      dataType: "json",  
      success:function(data) {
         alert(data[0]["name"]);
         for (i =0; i<data.length; i++)
  		{
  			names[i] = data[i]["name"];
  			longi[i] = data[i]["longitude"];
  			lat[i] = data[i]["latitude"];
  			rating[i] = data[i]["predictedRating"];
  			city[i] = data[i]["city"]
  		}
  		initialize2(longi, lat, names);
  		list(names,rating,city);
      }
   });
	/*for (i =0; i<data.length; i++)
  		{
  			names[i] = data[i]["name"];
  			longi[i] = data[i]["longitude"];
  			lat[i] = data[i]["latitude"];
  			rating[i] = data[i]["predictedRating"];
  			city[i] = data[i]["city"]
  		}
  		initialize2(longi, lat, names);
  		list(names,rating,city);*/
}
function initialize2(longi, lat, names) {
	var longit = longi;
	var latit = lat;
	var b_names = names
	c_longi = parseFloat(longit[0]);
	c_lati = parseFloat(latit[0]);
  	var mapOptions = {
    zoom: 10,
    center: new google.maps.LatLng(c_lati, c_longi)
  	}
  	var map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
  	var image = "/yelp/images/restaurant.png"
  	for (i =0; i < longit.length; i++)
  	{
  		longitude = parseFloat(longit[i]);
  		latitude = parseFloat(latit[i]);
    	var marker = new google.maps.Marker({
    	position: new google.maps.LatLng(latitude,longitude),
      	map: map,
      	icon:image,
      	animation: google.maps.Animation.DROP,
      	title: b_names[i]
  		});
  		google.maps.event.addListener(marker, 'click', toggleBounce);
	}
}

function toggleBounce() {

  if (marker.getAnimation() != null) {
    marker.setAnimation(null);
  } else {
    marker.setAnimation(google.maps.Animation.BOUNCE);
  }
}

google.maps.event.addDomListener(window, 'load', initialize);



function list(names,rating,city)
{
	var name = names;
	var cities = city;
	var ratings = rating;
	var i;
	len = name.length;
	loop_max = len/5;

	for (i=0; i<loop_max; i++)
	{
		jQuery('<div/>', {
    	id: 'entry'+i,
    	height: "100px",
    	width: "350px",
    	class: 'well'
		}).appendTo('#row_2_col_2');

		jQuery('<div/>', {
    	id: 'entry_img'+i,
    	class: 'col-md-3'
		}).appendTo('#entry'+i);

		jQuery('<div/>', {
    	id: 'entry_txt'+i,
    	class: 'col-md-9'
		}).appendTo('#entry'+i);
		

		$(function() {
    		$("#entry_img"+i).append(
       	 	$("<img />").attr({
            src: "images/food.png",
        		})
    		);
		});

		jQuery('<p/>', {
    	id: 'name'+i,
    	class: 'text-center'
		}).appendTo('#entry_txt'+i);
		document.getElementById('name'+i).innerHTML=name[i];

		jQuery('<p/>', {
    	id: 'rating'+i,
    	class: 'text-center'
		}).appendTo('#entry_txt'+i);
		document.getElementById('rating'+i).innerHTML= "Rating: "+ ratings[i];

		jQuery('<p/>', {
    	id: 'city'+i,
    	class: 'text-center'
		}).appendTo('#entry_txt'+i);
		document.getElementById('city'+i).innerHTML= cities[i];

	}
	document.getElementById('pagination').style.display = "block";
}

function next(num)
{
	page = num;
	var start = 4*(num-1);
	var end = 4*num;
	var i , j=0;
	for (i = start; i<end; i++)
	{
		document.getElementById('name'+j).innerHTML = names[i];
		document.getElementById('rating'+j).innerHTML = "Rating: "+ rating[i];
		document.getElementById('city'+j).innerHTML = city[i];
		j=j+1;
	}
}