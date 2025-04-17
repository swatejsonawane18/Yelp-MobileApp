const axios = require("axios")
const express= require("express");
const path = require('path');
const app = express();

app.use(express.static(path.resolve(__dirname, './build')));

app.get('/autocomplete', async function(request, response, next) {
    
  console.log("inside auto " + request.query.keyword);

  let resp=await axios.get("https://api.yelp.com/v3/autocomplete", {
    // signal,
    headers: {'Authorization': `Bearer ${YELP_KEY}`},
     params: {'text':request.query.keyword}
  })
  response.send(resp.data);
});




app.get('/iplocation', async function(request, response, next) {
    
    let resp = await axios.get(`https://ipinfo.io/?token={IP_LOCATION_TOKEN}`);
      console.log(resp.data);

    response.send(resp.data);
});

app.get('/googlelocation', async function(request, response, next) {

  console.log(request.query.user_location);
    
  let resp = await axios.get(`https://maps.googleapis.com/maps/api/geocode/json?address=${request.query.user_location}&key={GOOGLE_API_KEY}`);
    console.log(resp.data);

  response.send(resp.data);
});

app.get('/card_business', async function(request, response, next) {

    
  let resp = await axios.get(`https://api.yelp.com/v3/businesses/${request.query.id}`,{
  headers: {'Authorization': `Bearer {YELP_KEY}`},
  });
   
  response.send(resp.data);
});

app.get('/card_reviews', async function(request, response, next){

  let resp = await axios.get(`https://api.yelp.com/v3/businesses/${request.query.id}/reviews`,{
  headers: {'Authorization': `Bearer {YELP_KEY}`},
  });
  response.send(resp.data);
})

app.get('/business_search', async function(req, res, next) {
    
    console.log(req.query);
    let keyword=req.query.keyword;
    let distance=parseInt((parseInt(req.query.distance))*1609.94);
    let category=req.query.category;
    let latitude=parseFloat(req.query.latitude);
    let longitude=parseFloat(req.query.longitude);

    let resp2 = await axios.get('https://api.yelp.com/v3/businesses/search',{headers: {
        'Authorization': `Bearer {YELP_KEY}`
        },
      params: {
          'term': keyword, 
          'latitude': latitude ,
          'longitude': longitude ,
          'categories': category, 
          'radius' : distance,
          'limit':10
        }})
     res.send(resp2.data);
  });

  app.get('*', (req, res) => {
    res.sendFile(path.resolve(__dirname, './build', 'index.html'));
  });

  const PORT = process.env.PORT || 9000;
        app.listen(PORT, () => {
        console.log(`Server listening on port ${PORT}...`);
    });
