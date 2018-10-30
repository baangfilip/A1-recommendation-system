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
		$.each(data.recommendedMovies, function(score, movie) {
			score = parseFloat(score).toFixed(2);
			if(movies.length == 0)
				movies.push(`<li class="list-group-item"><span class="score first">${score}</span> ${movie.title} <span class="badge badge-pill badge-success">Best match!</span></li>`);
			else
				movies.push(`<li class="list-group-item"><span class="score">${score}</span> ${movie.title}</li>`);
				
		});
		if(movies.length){
			$("#movie-results ol").html(movies.join(""));
		}else{
			//no recommended movies
			$("#movie-results ol").html(`<small class="text-muted">Couldnt find any recommendations, this can be because you have watched all movies already or doesnt have any similar users</small>`);
		}

		let users = [];
		$.each(data.similarUsers, function(score, user) {
			score = parseFloat(score).toFixed(2);
			users.push(`<li class="list-group-item"><span class="score">${score}</span> ${user.userName}</li>`);
		});

		if(users.length){
			$("#user-results ol").html(users.join(""));
		}else{
			//no recommended users
			$("#user-results ol").html(`<div class="alert alert-secondary" role="alert">Start rating movies so we can find similar users</div>`);
		}
	});
}