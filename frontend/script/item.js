
  api_link = "http://localhost:8080/api"
  

  
 function item(book) {
  let container = document.querySelector(".container");

  let item_image = document.createElement("div");
  item_image.setAttribute("class", "item-image");

  let image_container = document.createElement("div");
  image_container.setAttribute("class", "book-image");
  let img = document.createElement("img");
  img.src = book.image_url;

  image_container.appendChild(img);
  item_image.appendChild(image_container);

  let info = document.createElement("div");
  info.setAttribute("class", "details");
  let title = document.createElement("h1");
  title.textContent = book.title;
  let author = document.createElement("h3");
  author.textContent = book.author;

  info.appendChild(title);
  info.appendChild(author);

  let add_info = document.createElement("div");
  add_info.setAttribute("class", "additional-info");
  let price = document.createElement('h4');
  price.setAttribute("id", "book-price");
  price.textContent = book.price + '$';
  let reviews = document.createElement('p');
  reviews.textContent = '483 reviews';
  
  let stars  = document.createElement("div");
  for (i=0;i<5;i++) {
	  let icon = document.createElement("i")
	  icon.setAttribute("class","bx bxs-star")
	  
	  stars.appendChild(icon);
  }

  add_info.appendChild(price);
  add_info.appendChild(stars);
  add_info.appendChild(reviews);
  info.appendChild(add_info);

  let desc = document.createElement('p');
  desc.textContent = book.description;
  info.appendChild(desc);

  let book_type = document.createElement('div');
  book_type.setAttribute("class", 'book-type');
  let paperback = document.createElement('button');
  paperback.textContent = 'Paperback';
  let hardcover = document.createElement('button');
  hardcover.textContent = 'HardCover';

  book_type.appendChild(paperback);
  book_type.appendChild(hardcover);
  info.appendChild(book_type);

  let quantity = document.createElement("div");
  quantity.setAttribute("class", "quantity");
  let text = document.createElement("h4");
  text.textContent = "Quantity";

  quantity.appendChild(text);

  let amount_price = document.createElement("div");
  amount_price.setAttribute("class", "amount-and-price");
  let add_remove = document.createElement('div');
  add_remove.setAttribute("class", "add-remove");
  let minus = document.createElement('button');
  minus.textContent = '-';
  let count = document.createElement("h2");
  count.textContent = '1';
  let plus = document.createElement("button");
  plus.textContent = '+';
  
  
  add_remove.appendChild(minus);
  add_remove.appendChild(count);
  add_remove.appendChild(plus);
  amount_price.appendChild(add_remove);

  let totalprice = document.createElement("h2");
  totalprice.textContent = book.price + "$";
  amount_price.appendChild(totalprice);
  quantity.appendChild(amount_price);
  info.appendChild(quantity);

  let cartbutton = document.createElement("button");
  cartbutton.setAttribute("id", "add-to-cart");
  cartbutton.textContent = 'Add to Cart';
  info.appendChild(cartbutton);
  
  /*
  const success_message = "Item added to cart successfully";
  const error_message = "An error occured";
  const message_div = document.createElement("div");
  message_div.setAttribute("id","message_div");
  const message_container = document.createElement("h4");
  message_container.innerHTML = success_message;
  message_div.appendChild(message_container);
  info.appendChild(message_div);
  */

  container.appendChild(item_image);
  container.appendChild(info);
  
  plus.addEventListener('click',(e) =>{
	  e.preventDefault();
	  count.textContent = parseInt(count.textContent) + 1;
	  totalprice.textContent = parseFloat(book.price) * parseInt(count.textContent) + "$";
  })
  
  
  minus.addEventListener('click', (e) => {
  e.preventDefault();
  if (parseInt(count.textContent) > 1) {
    count.textContent = parseInt(count.textContent) - 1;
	totalprice.textContent = parseFloat(book.price) * parseInt(count.textContent) + "$";
  }});
  
  cartbutton.addEventListener('click',async (e) =>{
	  e.preventDefault();
	  let payload = {
		  "BookId" : book.id,
		  "quantity" : count.textContent
	  }
	  
	  const success_message = "Item added to cart successfully";
	  const error_message = "An error occured";
	  const message_div = document.createElement("div");
	  message_div.setAttribute("id","message_div");
	  const message_container = document.createElement("h4");
			  
			  
	  try{
		  let response = await(axios.post(api_link + "/books/"+book.id+"/add",payload,{
				withCredentials: true,
				headers : {
				"Content-Type":"application/json;charset=UTF-8"
			}}
		  ));
		  
		  if(response.status == 200) {
			  console.log('item added to cart');
			  message_container.innerHTML = success_message;
			  message_container.style.color = "green";
			  message_div.appendChild(message_container);
			  message_div.style.display = "block";
			  info.appendChild(message_div);
			  
		  }
	  } catch (error) {
		  if(error.response) {
			  if(error.response.status){
				  console.log(error.response.status);
				  if(error.response.status === 401) {
					  window.location.href = "./signin.html";
				  }
			  }
			  
		  }else {
			  message_container.innerHTML = error_message;
			  message_container.style.color = "red";
			  message_div.appendChild(message_container);
			  message_div.style.display = "block";
			  info.appendChild(message_div);
		  }
		  
	  }
  })
  
}

    
  
  async function fetchBookData() {
	  //let books = [];
	  try{
		  let params = new URLSearchParams(document.location.search);
		  let id = params.get('id');
		  let response = await(axios.get(api_link + "/books/"+id,{
			  withCredentials: true,
				headers : {
				"Content-Type":"application/json;charset=UTF-8"
			}}))
			
		  if(response.status == 200) {
			  console.log('success')
			  book = response.data;
			  console.log(response);
			  item(book);
			  
		  }
	  } catch (error) {
		  if(error.response) {
			  if(error.response.status){
				  console.log(error.response.status);
				  if(error.response.status === 401) {
					  window.location.href = "./signin.html";
				  }
			  }
			  
		  }
	  }
  }
  
  
  
  
  function itemGrid(data,container_div) {
  container_div.innerHTML = "";
  
	  for (book of data) {
		let anchor  = document.createElement("a");
		
		anchor.setAttribute("href","./item.html?id="+book.id);
		
		
		let item = document.createElement("div");
		item.setAttribute("class", "item");
		
		let image_container = document.createElement("div");
		image_container.setAttribute("class", "image-container");
		let img = document.createElement("img");
		img.src = book.image_url;
		
		image_container.appendChild(img);
		item.appendChild(image_container);
		
		let info = document.createElement("div");
		info.setAttribute("class", "info");
		let title = document.createElement("h3");
		title.textContent = book.title;
		let rating = document.createElement("h3");
		rating.textContent = book.rating;
		
		info.appendChild(title);
		info.appendChild(rating);
		item.appendChild(info);
		
		item.addEventListener('click', () => {
		const itemurl = './item.html?' + 'id=' + book.id;
		window.location.href = itemurl;
		});
		
		anchor.appendChild(item);
		container_div.appendChild(anchor);
  }
}
  
  async function fetchData(url) {
	  let books = [];
	  try{
		  let response = await(axios.get(url,{
			  withCredentials: true,
				headers : {
				"Content-Type":"application/json;charset=UTF-8"
			}}))
		  if(response.status == 200) {
			  books = response.data._embedded.book_DTOes;
			  return books;
			  
			  
		  }
	  } catch (error) {
		  if(error.response) {
			  if(error.response.status){
				  console.log(error.response.status);
				  if(error.response.status === 401) {
					  window.location.href = "./signin.html";
				  }
			  }
			  
		  }
	  }
  }
  
   const search_button = document.getElementById("search--icon");
   search_button.addEventListener("click", async (e) =>{
	  e.preventDefault();
	  const search_value = document.getElementById("search-input").value;
	  
	  window.location.href = "./search.html?query="+search_value;
	  
	  
  })
  
    
const search_input = document.getElementById("search-input");

search_input.addEventListener('keydown',(event) =>{
	if(event.key == 'Enter'){
	  const search_value = document.getElementById("search-input").value;
	  
	  window.location.href = "./search.html?query="+search_value;
	  
		
	}
})
  
  
  
  
  window.onload = async () => {
	 fetchBookData();
	 const data = await fetchData(api_link + "/books");
	 const container = document.querySelector("#shop-similar");
	 itemGrid(data,container);
  };