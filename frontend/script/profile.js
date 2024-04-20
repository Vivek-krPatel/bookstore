const api_link = "http://localhost:8080/api/users"



const edit_profile = document.getElementById("edit-profile-button")

const myprofile = document.getElementById("myprofile");

const profile  = document.getElementsByClassName("profile")[0];

const edit  = document.getElementsByClassName("edit-profile")[0];

const mycart_button = document.getElementById("mycart");

const cart = document.getElementsByClassName("cart")[0];

const myorders_button = document.getElementById("myorders");

const cart_checkout = document.getElementsByClassName("cart-checkout")[0];

const logout_button = document.getElementById("logout");



async function fetchData(url) {
	  let data;
	  try{
		  let response = await(axios.get(url,{
			  withCredentials: true,
				headers : {
				"Content-Type":"application/json;charset=UTF-8"
			}}))
		  if(response.status == 200) {
			  console.log(response.data);
			  data = response.data;
			  console.log(data);
			  return data;
			  
			  
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
		  else if (error.request) {
			  console.log("No response received from the server");
			} else {
			  console.log("Error", error.message);
			}
	  }
  }
  
  
  async function view_profile() {
	  try {
		const user_info = await fetchData(api_link + "/profile");
		
		const firstname = document.getElementById("firstname");
		firstname.innerHTML = user_info.firstName;
		
		const lastname = document.getElementById("lastname");
		lastname.innerHTML = user_info.lastName;
		
		const email = document.getElementById("email");
		email.innerHTML = user_info.email;
		
		const phone = document.getElementById("phone");
		phone.innerHTML = user_info.phone;
		} catch (error) {
		if(error.response) {
			  if(error.response.status){
				  console.log(error.response.status);
				  if(error.response.status === 401) {
					  window.location.href = "./signin.html";
				  }
			  }
			  
		  }
		  else if (error.request) {
			  console.log("No response received from the server");
			} else {
			  console.log("Error", error.message);
			}
		}
		
		cart.style.display = "none";
		cart_checkout.style.display = "none";
		edit.style.display = "none";
		
		profile.style.display = "flex";
  }
  
  
async function fetchCart(){
	try {
		const cartDiv = document.getElementsByClassName("cart")[0];
		cartDiv.innerHTML = "";
		const cartData = await fetchData(api_link + "/mycart");
		const inside = document.createElement("div");
		inside.setAttribute("class","inside");
		
		
		const info = document.createElement("div");
		info.setAttribute("class","info");
		
		const heading = document.createElement("h2");
		heading.innerHTML = "Cart";
		info.appendChild(heading);
		
		const subheading = document.createElement("div");
		subheading.setAttribute("class","heading");
		
		const subh1 = document.createElement("P");
		subh1.innerHTML = "Books in Cart";
		
		const subh2 = document.createElement("p");
		subh2.innerHTML = "Quantity";
		
		subheading.appendChild(subh1);
		subheading.appendChild(subh2);
		
		info.appendChild(subheading);
		
		const cartitems = document.createElement("div");
		cartitems.setAttribute("class","cart-items");
		
		for (product of cartData){
			const item = document.createElement("div");
			item.setAttribute("class","item");
			
			const img = document.createElement("img")
			img.setAttribute("src",product.book.image_url);
			item.appendChild(img);
			
			const item_info = document.createElement("div");
			const title = document.createElement("h4");
			title.innerHTML = product.book.title;
			const author = document.createElement("p");
			author.innerHTML = product.book.author;
			
			item_info.appendChild(title);
		    item_info.appendChild(author);
			
			
			item.appendChild(item_info);
			
			const quantity_div = document.createElement("div");
			quantity_div.setAttribute("class","quantity");
			
			const quantity = document.createElement("p");
			quantity.innerHTML = product.quantity;
			
			quantity_div.appendChild(quantity);
			
			item.appendChild(quantity_div);
			
			cartitems.appendChild(item);
			
		}
		
		const checkout = document.createElement("div");
		checkout.setAttribute("id","checkout");
		
		const checkout_button = document.createElement("button");
		checkout_button.setAttribute("id","checkout_button");
		checkout_button.innerHTML = "Checkout";
		
		checkout.appendChild(checkout_button);
		
		info.appendChild(cartitems);
		info.appendChild(checkout);
		inside.appendChild(info);
		
		cartDiv.appendChild(inside);
		
		
		
		checkout_button.addEventListener("click", (e) => {
		e.preventDefault();
		checkout_cart()
		
		
		cart.style.display = "none";
		profile.style.display = "none";
		edit.style.display = "none";
		
		cart_checkout.style.display = "flex";
	
	
		})
				
	}catch (error) {
		if(error.response) {
			  if(error.response.status){
				  console.log(error.response.status);
				  if(error.response.status === 401) {
					  window.location.href = "./signin.html";
				  }
			  }
			  
		  }
		  else if (error.request) {
			  console.log("No response received from the server");
			} else {
			  console.log("Error", error.message);
			}
	}

	
	
}


async function checkout_cart(){
	try {
		const checkoutDiv = document.getElementsByClassName("cart-checkout")[0];
		checkoutDiv.innerHTML = "";
		const cartData = await fetchData(api_link + "/mycart");
		const inside = document.createElement("div");
		inside.setAttribute("class","inside");
		
		
		const info = document.createElement("div");
		info.setAttribute("class","info");
		
		const heading = document.createElement("h2");
		heading.innerHTML = "Checkout";
		info.appendChild(heading);
		
		const subheading = document.createElement("div");
		subheading.setAttribute("class","heading");
		
		const subh1 = document.createElement("P");
		subh1.innerHTML = "Books in Cart";
		
		const subh2 = document.createElement("p");
		subh2.innerHTML = "Subtotal";
		
		subheading.appendChild(subh1);
		subheading.appendChild(subh2);
		
		info.appendChild(subheading);
		
		const cartitems = document.createElement("div");
		cartitems.setAttribute("class","cart-items");
		
		for (product of cartData){
			const item = document.createElement("div");
			item.setAttribute("class","item");
			
			const img = document.createElement("img")
			img.setAttribute("src",product.book.image_url);
			item.appendChild(img);
			
			const item_info = document.createElement("div");
			const quantity = document.createElement("h4");
			quantity.innerHTML = "Quantity";
			const value = document.createElement("p");
			value.innerHTML = product.quantity;
			
			item_info.appendChild(quantity);
		    item_info.appendChild(value);
			
			
			item.appendChild(item_info);
			
			const subtotal_div = document.createElement("div");
			subtotal_div.setAttribute("class","quantity");
			
			const subtotal = document.createElement("p");
			subtotal.innerHTML = product.price + "$";
			
			subtotal_div.appendChild(subtotal);
			
			item.appendChild(subtotal_div);
			
			cartitems.appendChild(item);
			
		}
		
		const pay = document.createElement("div");
		pay.setAttribute("id","pay");
		
		const pay_button = document.createElement("button");
		pay_button.setAttribute("id","pay-button");
		pay_button.innerHTML = "Proceed to pay";
		
		pay.appendChild(pay_button);
		
		info.appendChild(cartitems);
		info.appendChild(pay);
		inside.appendChild(info);
		
		checkoutDiv.appendChild(inside);
		
		
		
		pay_button.addEventListener("click", (e) => {
			e.preventDefault();
			alert("order placed succesfully");
		/*
		cart.style.display = "none";
		profile.style.display = "none";
		edit.style.display = "none";
		
		cart_checkout.style.display = "flex";
	
		*/
		})
				
	}catch (error) {
		if(error.response) {
			  if(error.response.status){
				  console.log(error.response.status);
				  if(error.response.status === 401) {
					  window.location.href = "./signin.html";
				  }
			  }
			  
		  }
		  else if (error.request) {
			  console.log("No response received from the server");
			} else {
			  console.log("Error", error.message);
			}
	}

	
	
}



edit_profile.addEventListener("click", async (e) => {
	
	 try {
		const user_info = await fetchData(api_link + "/profile");
		
		const edit_firstname = document.getElementById("edit-firstname");
		edit_firstname.value = user_info.firstName;
		
		const edit_lastname = document.getElementById("edit-lastname");
		edit_lastname.value = user_info.lastName;
		
		const edit_email = document.getElementById("edit-email");
		edit_email.value = user_info.email;
		
		const edit_phone = document.getElementById("edit-phone");
		edit_phone.value = user_info.phone;
		
		
		const save_button = document.getElementById("save-profile-button");
		
		save_url = api_link + "/profile/edit";
		
			save_button.addEventListener("click", async (e) => {
				e.preventDefault();
				const payload = {
					"firstName" : edit_firstname.value,
					"lastName" : edit_lastname.value,
					"email" : edit_email.value,
					"phone" : edit_phone.value
				}
				
				try{
					  let response = await(axios.post(save_url,payload,{
							withCredentials: true,
							headers : {
							"Content-Type":"application/json;charset=UTF-8"
						}}
					  ));
					  
					  if(response.status == 200) {
						  console.log('success');
						  window.location.href = "./profile.html";
						  
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
			});
		
		
	} catch (error) {
		if(error.response) {
			  if(error.response.status){
				  console.log(error.response.status);
				  if(error.response.status === 401) {
					  window.location.href = "./signin.html";
				  }
			  }
			  
		  }
		  else if (error.request) {
			  console.log("No response received from the server");
			} else {
			  console.log("Error", error.message);
			}
	}
	
	
	
	
	
	
	e.preventDefault();
	profile.style.display = "none";
	cart_checkout.style.display = "none";
	
	cart.style.display = "none";
	
	edit.style.display = "flex";
	
})

mycart_button.addEventListener("click", async (e) => {
	e.preventDefault();
	
	fetchCart();
	profile.style.display = "none";
	cart_checkout.style.display = "none";
	edit.style.display = "none";
	
	cart.style.display = "flex";
	
})

myprofile.addEventListener("click", view_profile);


logout_button.addEventListener("click", async (e) => {
	e.preventDefault();
	const logout_url = api_link + "/logout";
	try{
					  let response = await(axios.get(logout_url,{
							withCredentials: true,
							headers : {
								"Content-Type":"application/json;charset=UTF-8"
							}
						}));
					  
					  if(response.status == 204) {
						  console.log('logout success');
						  window.location.href = "./index.html";
						  
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
				
			 
		  
					  else if (error.request) {
						  console.log("No response received from the server");
						} else {
						  console.log("Error", error.message);
						}
		}
	
	//cart.style.display = "none";
	//profile.style.display = "none";
	//edit.style.display = "none";
	
	//cart_checkout.style.display = "flex";
	//initial = 7139664FE661F2DCC008FBF7A35B8F86
	//after log in = 257B00217ADFD4C8395E871C9CA1F2A4
	
	
})




window.onload = async () => {
	view_profile();
}