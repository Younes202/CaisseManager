	<%@page import="framework.model.common.util.StringUtil"%>
<%@page import="appli.controller.domaine.util_erp.ContextAppli"%>
<div class="d-none">
		<div class="bg-primary p-3 d-flex align-items-center">
			<a class="toggle togglew toggle-2" href="#"><span></span></a>
			<h4 class="font-weight-bold m-0 text-white"><%=StringUtil.getValueOrEmpty(ContextAppli.getEtablissementBean().getRaison_sociale()) %></h4>
		</div>
	</div>
	<div class="offer-section py-4">
		<div class="container position-relative">
			<img alt="#" src="domaine/caisse/mobile-market/template/resources/img/trending1.png" style="margin-top: -14px;" class="restaurant-pic">
			<div class="pt-3 text-white">
<!-- 				<h2 class="font-weight-bold">Conrad Chicago Restaurant</h2> -->
				<p class="text-white m-0"><%=StringUtil.getValueOrEmpty(ContextAppli.getEtablissementBean().getAdresse()) %></p>
				<div class="rating-wrap d-flex align-items-center mt-2">
					<ul class="rating-stars list-unstyled">
						<li>
							<i class="feather-star text-warning"></i> 
							<i class="feather-star text-warning"></i> 
							<i class="feather-star text-warning"></i> 
							<i class="feather-star text-warning"></i> 
							<i class="feather-star"></i>
						</li>
					</ul>
					<p class="label-rating text-white ml-2 small">(245 avis)</p>
				</div>
			</div>
			<div class="pb-4">
				<div class="row">
					<div class="col-6 col-md-2">
						<p class="text-white-50 font-weight-bold m-0 small">Livraison</p>
						<p class="text-white m-0">Gratuite</p>
					</div>
					<div class="col-6 col-md-2">
						<p class="text-white-50 font-weight-bold m-0 small">Horaires</p>
						<p class="text-white m-0">8:00-22:00</p>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="container" style=" margin-top: 19px;">
		<div class="p-3 bg-primary bg-primary mt-n3 rounded position-relative">
			<div class="d-flex align-items-center">
				<div class="feather_icon">
					<a href="#ratings-and-reviews" class="text-decoration-none text-dark">
						<i class="p-2 bg-light rounded-circle font-weight-bold  feather-upload"></i>
					</a> 
					<a href="#ratings-and-reviews" class="text-decoration-none text-dark mx-2">
						<i class="p-2 bg-light rounded-circle font-weight-bold  feather-star"></i>
					</a> 
					<a href="#ratings-and-reviews" class="text-decoration-none text-dark">
						<i class="p-2 bg-light rounded-circle font-weight-bold feather-map-pin"></i>
					</a>
				</div>
				<a href="contact-us.html" class="btn btn-sm btn-outline-light ml-auto">Mes coordonnées</a>
			</div>
		</div>
	</div>
	<div class="container position-relative">
		<div class="row">
			<div class="col-md-8 pt-3">
				<div class="shadow-sm rounded bg-white mb-3 overflow-hidden">
					<div class="d-flex item-aligns-center">
						<p class="font-weight-bold h6 p-3 border-bottom mb-0 w-100">Menu</p>

					</div>
					<div class="row m-0">
						<h6 class="p-3 m-0 bg-light w-100">
							Quick Bites <small class="text-black-50">3 ITEMS</small>
						</h6>
						<div class="col-md-12 px-0 border-top">
							<div class>
								<div class="p-3 border-bottom gold-members">
									<span class="float-right"><a href="#" class="btn btn-outline-secondary btn-sm" data-toggle="modal" data-target="#extras_modal">ADD</a></span>
									<div class="media">
										<div class="mr-3 font-weight-bold text-danger non_veg">.</div>
										<div class="media-body">
											<h6 class="mb-1">Chicken Tikka Sub</h6>
											<p class="text-muted mb-0">$250</p>
										</div>
									</div>
								</div>
								<div class="p-3 border-bottom gold-members">
									<span class="float-right"><a href="#" class="btn btn-outline-secondary btn-sm" data-toggle="modal" data-target="#extras_modal">ADD</a></span>
									<div class="media">
										<div class="mr-3 font-weight-bold text-danger non_veg">.</div>
										<div class="media-body">
											<h6 class="mb-1">
												Cheese corn Roll <span class="badge badge-danger">BEST SELLER</span>
											</h6>
											<p class="text-muted mb-0">$600</p>
										</div>
									</div>
								</div>
								<div class="p-3 border-bottom gold-members">
									<span class="float-right"><a href="#" class="btn btn-outline-secondary btn-sm" data-toggle="modal" data-target="#extras_modal">ADD</a></span>
									<div class="media">
										<div class="mr-3 font-weight-bold text-danger non_veg">.</div>
										<div class="media-body">
											<h6 class="mb-1">
												Chicken Tikka Sub <span class="badge badge-danger text-white">Non veg</span>
											</h6>
											<p class="text-muted mb-0">$250</p>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="row m-0">
						<h6 class="p-3 m-0 bg-light w-100">
							Starters <small class="text-black-50">3 ITEMS</small>
						</h6>
						<div class="col-md-12 px-0 border-top">
							<div class>
								<div class="p-3 border-bottom menu-list">
									<span class="float-right"><a href="#" class="btn btn-outline-secondary btn-sm" data-toggle="modal" data-target="#extras_modal">ADD</a></span>
									<div class="media">
										<img alt="#" src="domaine/caisse/mobile-market/template/resources/img/starter1.jpg" alt="Gurdeep Osahan" class="mr-3 rounded-pill ">
										<div class="media-body">
											<h6 class="mb-1">Chicken Tikka Sub</h6>
											<p class="text-muted mb-0">$250</p>
										</div>
									</div>
								</div>
								<div class="p-3 border-bottom menu-list">
									<span class="float-right"><a href="#" class="btn btn-outline-secondary btn-sm" data-toggle="modal" data-target="#extras_modal">ADD</a></span>
									<div class="media">
										<img alt="#" src="domaine/caisse/mobile-market/template/resources/img/starter2.jpg" alt="Gurdeep Osahan" class="mr-3 rounded-pill ">
										<div class="media-body">
											<h6 class="mb-1">
												Cheese corn Roll <span class="badge badge-danger">BEST SELLER</span>
											</h6>
											<p class="text-muted mb-0">$600</p>
										</div>
									</div>
								</div>
								<div class="p-3 border-bottom menu-list">
									<span class="float-right"><a href="#" class="btn btn-outline-secondary btn-sm" data-toggle="modal" data-target="#extras_modal">ADD</a></span>
									<div class="media">
										<img alt="#" src="domaine/caisse/mobile-market/template/resources/img/starter3.jpg" alt="Gurdeep Osahan" class="mr-3 rounded-pill ">
										<div class="media-body">
											<h6 class="mb-1">
												Chicken Tikka Sub <span class="badge badge-success">Pure Veg</span>
											</h6>
											<p class="text-muted mb-0">$250</p>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="row m-0">
						<h6 class="p-3 m-0 bg-light w-100">
							Soups <small class="text-black-50">8 ITEMS</small>
						</h6>
						<div class="col-md-12 px-0 border-top">
							<div class="bg-white">
								<div class="p-3 border-bottom gold-members">
									<span class="float-right"><a href="#" class="btn btn-outline-secondary btn-sm" data-toggle="modal" data-target="#extras_modal">ADD</a></span>
									<div class="media">
										<div class="mr-3 font-weight-bold text-danger non_veg">.</div>
										<div class="media-body">
											<h6 class="mb-1">Chicken Tikka Sub</h6>
											<p class="text-muted mb-0">$250</p>
										</div>
									</div>
								</div>
								<div class="p-3 border-bottom gold-members">
									<span class="float-right"><a href="#" class="btn btn-outline-secondary btn-sm" data-toggle="modal" data-target="#extras_modal">ADD</a></span>
									<div class="media">
										<div class="mr-3 font-weight-bold text-danger non_veg">.</div>
										<div class="media-body">
											<h6 class="mb-1">
												Cheese corn Roll <span class="badge badge-danger">BEST SELLER</span>
											</h6>
											<p class="text-muted mb-0">$600</p>
										</div>
									</div>
								</div>
								<div class="p-3 border-bottom gold-members">
									<span class="float-right"><a href="#" class="btn btn-outline-secondary btn-sm" data-toggle="modal" data-target="#extras_modal">ADD</a></span>
									<div class="media">
										<div class="mr-3 font-weight-bold text-success veg">.</div>
										<div class="media-body">
											<h6 class="mb-1">
												Chicken Tikka Sub <span class="badge badge-success">Pure Veg</span>
											</h6>
											<p class="text-muted mb-0">$250</p>
										</div>
									</div>
								</div>
								<div class="p-3 border-bottom gold-members">
									<span class="float-right"><a href="#" class="btn btn-outline-secondary btn-sm" data-toggle="modal" data-target="#extras_modal">ADD</a></span>
									<div class="media">
										<div class="mr-3 font-weight-bold text-success veg">.</div>
										<div class="media-body">
											<h6 class="mb-1">Chicken Tikka Sub</h6>
											<p class="text-muted mb-0">$250</p>
										</div>
									</div>
								</div>
								<div class="p-3 border-bottom gold-members">
									<span class="float-right"><a href="#" class="btn btn-outline-secondary btn-sm" data-toggle="modal" data-target="#extras_modal">ADD</a></span>
									<div class="media">
										<div class="mr-3 font-weight-bold text-danger non_veg">.</div>
										<div class="media-body">
											<h6 class="mb-1">
												Cheese corn Roll <span class="badge badge-danger">BEST SELLER</span>
											</h6>
											<p class="text-muted mb-0">$600</p>
										</div>
									</div>
								</div>
								<div class="p-3 gold-members">
									<span class="float-right"><a href="#" class="btn btn-outline-secondary btn-sm" data-toggle="modal" data-target="#extras_modal">ADD</a></span>
									<div class="media">
										<div class="mr-3 font-weight-bold text-success veg">.</div>
										<div class="media-body">
											<h6 class="mb-1">
												Chicken Tikka Sub <span class="badge badge-success">Pure Veg</span>
											</h6>
											<p class="text-muted mb-0">$250</p>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="mb-3">
					<div id="ratings-and-reviews" class="bg-white shadow-sm d-flex align-items-center rounded p-3 mb-3 clearfix restaurant-detailed-star-rating">
						<h6 class="mb-0">Rate this Place</h6>
						<div class="star-rating ml-auto">
							<div class="d-inline-block h6 m-0">
								<i class="feather-star text-warning"></i> <i class="feather-star text-warning"></i> <i class="feather-star text-warning"></i> <i class="feather-star text-warning"></i> <i class="feather-star"></i>
							</div>
							<b class="text-black ml-2">334</b>
						</div>
					</div>
					<div class="bg-white rounded p-3 mb-3 clearfix graph-star-rating rounded shadow-sm">
						<h6 class="mb-0 mb-1">Ratings and Reviews</h6>
						<p class="text-muted mb-4 mt-1 small">Rated 3.5 out of 5</p>
						<div class="graph-star-rating-body">
							<div class="rating-list">
								<div class="rating-list-left font-weight-bold small">5 Star</div>
								<div class="rating-list-center">
									<div class="progress">
										<div role="progressbar" class="progress-bar bg-info" aria-valuenow="56" aria-valuemin="0" aria-valuemax="100" style="width: 56%;"></div>
									</div>
								</div>
								<div class="rating-list-right font-weight-bold small">56 %</div>
							</div>
							<div class="rating-list">
								<div class="rating-list-left font-weight-bold small">4 Star</div>
								<div class="rating-list-center">
									<div class="progress">
										<div role="progressbar" class="progress-bar bg-info" aria-valuenow="23" aria-valuemin="0" aria-valuemax="100" style="width: 23%;"></div>
									</div>
								</div>
								<div class="rating-list-right font-weight-bold small">23 %</div>
							</div>
							<div class="rating-list">
								<div class="rating-list-left font-weight-bold small">3 Star</div>
								<div class="rating-list-center">
									<div class="progress">
										<div role="progressbar" class="progress-bar bg-info" aria-valuenow="11" aria-valuemin="0" aria-valuemax="100" style="width: 11%;"></div>
									</div>
								</div>
								<div class="rating-list-right font-weight-bold small">11 %</div>
							</div>
							<div class="rating-list">
								<div class="rating-list-left font-weight-bold small">2 Star</div>
								<div class="rating-list-center">
									<div class="progress">
										<div role="progressbar" class="progress-bar bg-info" aria-valuenow="6" aria-valuemin="0" aria-valuemax="100" style="width: 6%;"></div>
									</div>
								</div>
								<div class="rating-list-right font-weight-bold small">6 %</div>
							</div>
							<div class="rating-list">
								<div class="rating-list-left font-weight-bold small">1 Star</div>
								<div class="rating-list-center">
									<div class="progress">
										<div role="progressbar" class="progress-bar bg-info" aria-valuenow="4" aria-valuemin="0" aria-valuemax="100" style="width: 4%;"></div>
									</div>
								</div>
								<div class="rating-list-right font-weight-bold small">4 %</div>
							</div>
						</div>
						<div class="graph-star-rating-footer text-center mt-3">
							<button type="button" class="btn btn-primary btn-block btn-sm">Rate and Review</button>
						</div>
					</div>
					<div class="bg-white p-3 mb-3 restaurant-detailed-ratings-and-reviews shadow-sm rounded">
						<a class="text-primary float-right" href="#">Top Rated</a>
						<h6 class="mb-1">All Ratings and Reviews</h6>
						<div class="reviews-members py-3">
							<div class="media">
								<a href="#"><img alt="#" src="domaine/caisse/mobile-market/template/resources/img/reviewer1.png" class="mr-3 rounded-pill"></a>
								<div class="media-body">
									<div class="reviews-members-header">
										<div class="star-rating float-right">
											<div class="d-inline-block" style="font-size: 14px;">
												<i class="feather-star text-warning"></i> <i class="feather-star text-warning"></i> <i class="feather-star text-warning"></i> <i class="feather-star text-warning"></i> <i class="feather-star"></i>
											</div>
										</div>
										<h6 class="mb-0">
											<a class="text-dark" href="#">Trump</a>
										</h6>
										<p class="text-muted small">Tue, 20 Mar 2020</p>
									</div>
									<div class="reviews-members-body">
										<p>Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of classNameical Latin literature from 45 BC, making it over 2000 years old.</p>
									</div>
									<div class="reviews-members-footer">
										<a class="total-like btn btn-sm btn-outline-primary" href="#"><i class="feather-thumbs-up"></i> 856M</a> <a class="total-like btn btn-sm btn-outline-primary" href="#"><i class="feather-thumbs-down"></i> 158K</a> <span class="total-like-user-main ml-2" dir="rtl"> <a href="#" aria-describedby="tooltip-top0"><img alt="#" src="domaine/caisse/mobile-market/template/resources/img/reviewer3.png" class="total-like-user rounded-pill"></a> <a href="#" aria-describedby="tooltip-top1"><img alt="#" src="domaine/caisse/mobile-market/template/resources/img/reviewer4.png" class="total-like-user rounded-pill"></a> <a href="#"><img alt="#" src="domaine/caisse/mobile-market/template/resources/img/reviewer5.png" class="total-like-user rounded-pill"></a> <a href="#" aria-describedby="tooltip-top3"><img alt="#" src="domaine/caisse/mobile-market/template/resources/img/reviewer6.png"
												class="total-like-user rounded-pill"></a>
										</span>
									</div>
								</div>
							</div>
						</div>
						<hr>
						<div class="reviews-members py-3">
							<div class="media">
								<a href="#"><img alt="#" src="domaine/caisse/mobile-market/template/resources/img/reviewer2.png" class="mr-3 rounded-pill"></a>
								<div class="media-body">
									<div class="reviews-members-header">
										<div class="star-rating float-right">
											<div class="d-inline-block" style="font-size: 14px;">
												<i class="feather-star text-warning"></i> <i class="feather-star text-warning"></i> <i class="feather-star text-warning"></i> <i class="feather-star text-warning"></i> <i class="feather-star"></i>
											</div>
										</div>
										<h6 class="mb-0">
											<a class="text-dark" href="#">Jhon Smith</a>
										</h6>
										<p class="text-muted small">Tue, 20 Mar 2020</p>
									</div>
									<div class="reviews-members-body">
										<p>It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout.</p>
									</div>
									<div class="reviews-members-footer">
										<a class="total-like btn btn-sm btn-outline-primary" href="#"><i class="feather-thumbs-up"></i> 88K</a> <a class="total-like btn btn-sm btn-outline-primary" href="#"><i class="feather-thumbs-down"></i> 1K</a><span class="total-like-user-main ml-2" dir="rtl"><a href="#"><img alt="#" src="domaine/caisse/mobile-market/template/resources/img/reviewer3.png" class="total-like-user rounded-pill"></a><a href="#"><img alt="#" src="domaine/caisse/mobile-market/template/resources/img/reviewer4.png" class="total-like-user rounded-pill"></a><a href="#"><img alt="#" src="domaine/caisse/mobile-market/template/resources/img/reviewer5.png" class="total-like-user rounded-pill"></a><a href="#"><img alt="#" src="domaine/caisse/mobile-market/template/resources/img/reviewer6.png" class="total-like-user rounded-pill"></a></span>
									</div>
								</div>
							</div>
						</div>
						<hr>
						<a class="text-center w-100 d-block mt-3 font-weight-bold" href="#">See All Reviews</a>
					</div>
					<div class="bg-white p-3 rating-review-select-page rounded shadow-sm">
						<h6 class="mb-3">Leave Comment</h6>
						<div class="d-flex align-items-center mb-3">
							<p class="m-0 small">Rate the Place</p>
							<div class="star-rating ml-auto">
								<div class="d-inline-block">
									<i class="feather-star text-warning"></i> <i class="feather-star text-warning"></i> <i class="feather-star text-warning"></i> <i class="feather-star text-warning"></i> <i class="feather-star"></i>
								</div>
							</div>
						</div>
						<form>
							<div class="form-group">
								<label class="form-label small">Your Comment</label>
								<textarea class="form-control"></textarea>
							</div>
							<div class="form-group mb-0">
								<button type="button" class="btn btn-primary btn-block">Submit Comment</button>
							</div>
						</form>
					</div>
				</div>
			</div>
			<div class="col-md-4 pt-3">
				<div class="osahan-cart-item rounded rounded shadow-sm overflow-hidden bg-white sticky_sidebar">
					<div class="d-flex border-bottom osahan-cart-item-profile bg-white p-3">
						<img alt="osahan" src="domaine/caisse/mobile-market/template/resources/img/starter1.jpg" class="mr-3 rounded-circle img-fluid">
						<div class="d-flex flex-column">
							<h6 class="mb-1 font-weight-bold">Spice Hut Indian Restaurant</h6>
							<p class="mb-0 small text-muted">
								<i class="feather-map-pin"></i> 2036 2ND AVE, NEW YORK, NY 10029
							</p>
						</div>
					</div>
					<div class="bg-white border-bottom py-2">
						<div class="gold-members d-flex align-items-center justify-content-between px-3 py-2 border-bottom">
							<div class="media align-items-center">
								<div class="mr-2 text-danger">&middot;</div>
								<div class="media-body">
									<p class="m-0">Chicken Tikka Sub</p>
								</div>
							</div>
							<div class="d-flex align-items-center">
								<span class="count-number float-right"><button type="button" class="btn-sm left dec btn btn-outline-secondary">
										<i class="feather-minus"></i>
									</button>
									<input class="count-number-input" type="text" readonly value="2">
								<button type="button" class="btn-sm right inc btn btn-outline-secondary">
										<i class="feather-plus"></i>
									</button></span>
								<p class="text-gray mb-0 float-right ml-2 text-muted small">$628</p>
							</div>
						</div>
						<div class="gold-members d-flex align-items-center justify-content-between px-3 py-2 border-bottom">
							<div class="media align-items-center">
								<div class="mr-2 text-danger">&middot;</div>
								<div class="media-body">
									<p class="m-0">Methi Chicken Dry</p>
								</div>
							</div>
							<div class="d-flex align-items-center">
								<span class="count-number float-right"><button type="button" class="btn-sm left dec btn btn-outline-secondary">
										<i class="feather-minus"></i>
									</button>
									<input class="count-number-input" type="text" readonly value="2">
								<button type="button" class="btn-sm right inc btn btn-outline-secondary">
										<i class="feather-plus"></i>
									</button></span>
								<p class="text-gray mb-0 float-right ml-2 text-muted small">$628</p>
							</div>
						</div>
						<div class="gold-members d-flex align-items-center justify-content-between px-3 py-2 border-bottom">
							<div class="media align-items-center">
								<div class="mr-2 text-danger">&middot;</div>
								<div class="media-body">
									<p class="m-0">Reshmi Kebab</p>
								</div>
							</div>
							<div class="d-flex align-items-center">
								<span class="count-number float-right"><button type="button" class="btn-sm left dec btn btn-outline-secondary">
										<i class="feather-minus"></i>
									</button>
									<input class="count-number-input" type="text" readonly value="2">
								<button type="button" class="btn-sm right inc btn btn-outline-secondary">
										<i class="feather-plus"></i>
									</button></span>
								<p class="text-gray mb-0 float-right ml-2 text-muted small">$628</p>
							</div>
						</div>
						<div class="gold-members d-flex align-items-center justify-content-between px-3 py-2 border-bottom">
							<div class="media align-items-center">
								<div class="mr-2 text-success">&middot;</div>
								<div class="media-body">
									<p class="m-0">Lemon Cheese Dry</p>
								</div>
							</div>
							<div class="d-flex align-items-center">
								<span class="count-number float-right"><button type="button" class="btn-sm left dec btn btn-outline-secondary">
										<i class="feather-minus"></i>
									</button>
									<input class="count-number-input" type="text" readonly value="2">
								<button type="button" class="btn-sm right inc btn btn-outline-secondary">
										<i class="feather-plus"></i>
									</button></span>
								<p class="text-gray mb-0 float-right ml-2 text-muted small">$628</p>
							</div>
						</div>
						<div class="gold-members d-flex align-items-center justify-content-between px-3 py-2">
							<div class="media align-items-center">
								<div class="mr-2 text-success">&middot;</div>
								<div class="media-body">
									<p class="m-0">Rara Paneer</p>
								</div>
							</div>
							<div class="d-flex align-items-center">
								<span class="count-number float-right"><button type="button" class="btn-sm left dec btn btn-outline-secondary">
										<i class="feather-minus"></i>
									</button>
									<input class="count-number-input" type="text" readonly value="2">
								<button type="button" class="btn-sm right inc btn btn-outline-secondary">
										<i class="feather-plus"></i>
									</button></span>
								<p class="text-gray mb-0 float-right ml-2 text-muted small">$628</p>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

		
	<div class="modal fade" id="extras_modal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-dialog-centered">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title">Extras</h5>
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
						<div class="recepie-body">
							<div class="custom-control custom-radio border-bottom py-2">
								<input type="radio" id="customRadio1f" name="location" class="custom-control-input" checked> <label class="custom-control-label" for="customRadio1f">Tuna <span class="text-muted">+$35.00</span></label>
							</div>
							<div class="custom-control custom-radio border-bottom py-2">
								<input type="radio" id="customRadio2f" name="location" class="custom-control-input"> <label class="custom-control-label" for="customRadio2f">Salmon <span class="text-muted">+$20.00</span></label>
							</div>
							<div class="custom-control custom-radio border-bottom py-2">
								<input type="radio" id="customRadio3f" name="location" class="custom-control-input"> <label class="custom-control-label" for="customRadio3f">Wasabi <span class="text-muted">+$25.00</span></label>
							</div>
							<div class="custom-control custom-radio border-bottom py-2">
								<input type="radio" id="customRadio4f" name="location" class="custom-control-input"> <label class="custom-control-label" for="customRadio4f">Unagi <span class="text-muted">+$10.00</span></label>
							</div>
							<div class="custom-control custom-radio border-bottom py-2">
								<input type="radio" id="customRadio5f" name="location" class="custom-control-input"> <label class="custom-control-label" for="customRadio5f">Vegetables <span class="text-muted">+$5.00</span></label>
							</div>
							<div class="custom-control custom-radio border-bottom py-2">
								<input type="radio" id="customRadio6f" name="location" class="custom-control-input"> <label class="custom-control-label" for="customRadio6f">Noodles <span class="text-muted">+$30.00</span></label>
							</div>
							<h6 class="font-weight-bold mt-4">QUANTITY</h6>
							<div class="d-flex align-items-center">
								<p class="m-0">1 Item</p>
								<div class="ml-auto">
									<span class="count-number"><button type="button" class="btn-sm left dec btn btn-outline-secondary">
											<i class="feather-minus"></i>
										</button>
										<input class="count-number-input" type="text" readonly value="1">
									<button type="button" class="btn-sm right inc btn btn-outline-secondary">
											<i class="feather-plus"></i>
										</button></span>
								</div>
							</div>
						</div>
				</div>
				<div class="modal-footer p-0 border-0">
					<div class="col-6 m-0 p-0">
						<button type="button" class="btn border-top btn-lg btn-block" data-dismiss="modal">Fermer</button>
					</div>
					<div class="col-6 m-0 p-0">
						<button type="button" class="btn btn-primary btn-lg btn-block">Appliquer</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<script type="a7fa0bc4ee59ea5ed9962834-text/javascript" src="domaine/caisse/mobile-market/template/resources/js/osahan.js"></script>
	<script src="domaine/caisse/mobile-market/template/resources/js/rocket-loader.min.js" data-cf-settings="a7fa0bc4ee59ea5ed9962834-|49" defer></script>
		