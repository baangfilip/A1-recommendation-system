$(function() {
	$.getJSON( "API/users", function( data ) {
		var users = [];
		users.push(`<option selected>Open this to choose user</option>`)
		$.each( data, function(key, user) {
			users.push(`<option value="${user.userID}">${user.userName}</option>`);
		});
		$("#users").html(users.join(""));
	});
	
	$("form").submit(function(event) {
		let formArr = $(this).serializeArray();
		getRec(formToJson(formArr));
		event.preventDefault();
	});
});

function formToJson(form){
	const json = {};
	const fields = form.values();
	for(const field of fields){
		json[field.name] = field.value || '';
	}
	return json;
}

function getRec(form){
	$.getJSON(`/API/rec/user/${form.similarityMeasure}/${form.userID}`, function(data) {
		let movies = [];
		$.each( data.recommendedMovies, function(key, movie) {
			if(movies.length == 0)
				movies.push(`<li class="list-group-item">${movie.score} ${movie.title} <span class="badge badge-pill badge-success">Best match!</span></li>`);
			else
				movies.push(`<li class="list-group-item">${movie.score} ${movie.title}</li>`);
				
		});
		$("#movie-results ol").html(movies.join(""));

		let users = [];
		$.each( data.similarUsers, function(key, user) {
			users.push(`<li class="list-group-item">${key} ${user.userName}</li>`);
		});
		$("#user-results ol").html(users.join(""));
	});
}