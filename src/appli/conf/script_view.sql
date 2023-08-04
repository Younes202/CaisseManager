DROP TABLE IF EXISTS `article_inv_max_view`;
DROP TABLE IF EXISTS `article_stock_inv_view`;
DROP TABLE IF EXISTS `article_stock_view`;
DROP TABLE IF EXISTS `caisse_vente_view`;
DROP TABLE IF EXISTS `journee_vente_view`;
DROP TABLE IF EXISTS `mouvement_stock_view`;

create or replace SQL SECURITY INVOKER view article_inv_max_view as 
	select max(det.id) as maxId, det.article_id, mv.emplacement_id, mv.etablissement_id, mv.societe_id, mv.abonne_id
		from mouvement_article det inner join mouvement mv on mv.id=det.mouvement_id   
		where mv.inventaire_id is not null 
		group by mv.emplacement_id, det.article_id;
	    	    
create or replace SQL SECURITY INVOKER view article_stock_inv_view as 
		select                                                                             
				art.id as article_id,                                                       
	            empl.id as emplacement_id,                                                 
	            SUM(                                                                       
					case                                                                    
						when ((type_mvmnt='a' or type_mvmnt='t' or type_mvmnt='tr' or type_mvmnt='rt') and mvm.destination_id=empl.id) then IFNULL(artmvm.quantite,0)
						when (type_mvmnt='i' and mvm.emplacement_id=empl.id) then IFNULL(artmvm.quantite,0) 
						else 0                                                                                        
					end                                                                                               
				) as qte_entree,                                                                                      
		        SUM(                                                                                                 
					case                                                                                              
						when ((type_mvmnt='v' or type_mvmnt='av' or type_mvmnt='vc' or type_mvmnt='t' or type_mvmnt='p' or type_mvmnt='c' or type_mvmnt='tr') and mvm.emplacement_id=empl.id) then IFNULL(artmvm.quantite,0)
					else 0                                                                                                                                                   
					end                                                                                                                                                      
			) as qte_sortie,
			mvm.etablissement_id,
			mvm.societe_id, mvm.abonne_id
	    from mouvement mvm                                                                                                                                                  
	    inner join mouvement_article artmvm on mvm.id=artmvm.mouvement_id                                                                                                   
		inner join article art on artmvm.article_id=art.id                                                                                                                  
	    left join emplacement empl on (mvm.emplacement_id=empl.id or mvm.destination_id=empl.id)                                                                            
	    left join article_inv_max_view invArt on (invArt.article_id=artmvm.article_id and invArt.emplacement_id=empl.id)
	    where art.is_stock = 1 and artmvm.id>=IFNULL(invArt.maxId, 0)                                                                                                    
		group by empl.id, art.id;

create or replace SQL SECURITY INVOKER view article_stock_view as 
	select 
			art.id as article_id, 
			art.code as article_code, 
			art.libelle as article_lib, 
			emplSeuil.qte_seuil as seuil,
			
			art.prix_achat_ht as prix_achat_ht,
			val.libelle as taux_tva,
			(IFNULL(art.prix_achat_ht,0) * val.libelle)/100 as mtt_tva,
			(IFNULL(art.prix_achat_ht,0) + ((IFNULL(art.prix_achat_ht,0) * val.code)/100)) as prix_achat_ttc,
			avg(case 
					when (type_mvmnt='a') then IFNULL(artmvm.prix_ht,0) 
				end
			) as prix_achat_moyen_ht, 
			
			fam.id as famille_id,
			fam.b_left as famille_bleft,
			fam.code as famille_code,
			fam.libelle as famille_lib,
            empl.id as emplacement_id,
            empl.titre as emplacement_titre,
            invArt.qte_entree as qte_entree, 
	        invArt.qte_sortie as qte_sortie,
	        mvm.etablissement_id,
	        mvm.societe_id, mvm.abonne_id
        from mouvement mvm 
        inner join mouvement_article artmvm on mvm.id=artmvm.mouvement_id 
		inner join article art on artmvm.article_id=art.id 
		inner join famille fam on fam.id=art.famille_stock_id 
        left join emplacement empl on (mvm.emplacement_id=empl.id or mvm.destination_id=empl.id)
        left join val_type_enumere val on val.id=art.tva_enum_id
        left join emplacement_seuil emplSeuil on emplSeuil.composant_id=art.id
        -- On join avec la vue des quantité suivant l'inventaire
        left join article_stock_inv_view invArt on invArt.emplacement_id=empl.id and invArt.article_id=art.id
		where art.is_stock = 1
		group by empl.id, art.id;
		

create or replace SQL SECURITY INVOKER view caisse_vente_view as
		select 
			cai.reference as caisse_reference,
			mvm.caisse_journee_id as caisse_journee_id,
			jour.id as journee_id,
			jour.date_journee as date_journee,
			jour.statut_journee as statut_journee,
    		cj.date_ouverture as date_ouverture,
    		cj.date_cloture as date_cloture,
			cj.statut_caisse as statut_caisse,
			cj.caisse_id as caisse_id,
			cj.mtt_ouverture as mtt_ouverture,
			cj.mtt_cloture_caissier as mtt_cloture_caissier,
			
			cj.mtt_cloture_caissier_espece as mtt_cloture_caissier_espece,
    		cj.mtt_cloture_caissier_cb as mtt_cloture_caissier_cb,
    		cj.mtt_cloture_caissier_cheque as mtt_cloture_caissier_cheque,
    		cj.mtt_cloture_caissier_dej as mtt_cloture_caissier_dej,
    		
    		cj.mtt_cloture_old_espece as mtt_cloture_old_espece,
    		cj.mtt_cloture_old_cb as mtt_cloture_old_cb,
    		cj.mtt_cloture_old_cheque as mtt_cloture_old_cheque,
    		cj.mtt_cloture_old_dej as mtt_cloture_old_dej,
    		us.login as user_cloture,
    		SUM( 
				case  
					when (last_statut!='ANNUL' and mvm.type_commande='L') then 1 
					else 0 
				end 
			) as nbr_livraison, 
			SUM( 
				case  
					when (last_statut!='ANNUL') then IFNULL(mvm.mtt_donne_cb,0) 
					else 0 
				end 
			) as mtt_cb, 
			SUM( 
				case  
					when (last_statut!='ANNUL') then IFNULL(mvm.mtt_donne_cheque,0) 
					else 0 
				end 
			) as mtt_cheque,
			SUM( 
				case  
					when (last_statut!='ANNUL') then IFNULL(mvm.mtt_donne_dej,0) 
					else 0 
				end 
			) as mtt_dej,
			SUM( 
				case  
					when (last_statut!='ANNUL') then IFNULL(mvm.mtt_donne,0) 
					else 0 
				end 
			) as mtt_espece, 
			SUM( 
				case  
					when (last_statut!='ANNUL') then IFNULL(mvm.mtt_annul_ligne,0) 
					else 0 
				end 
			) as mtt_annul_ligne, 
			SUM( 
			case  
				when (last_statut='ANNUL') then IFNULL(mvm.mtt_commande_net,0) 
				else 0 
			end 
			) as mtt_annule, 
			SUM( 
		     case 
		     when (last_statut!='ANNUL') then IFNULL(mvm.mtt_commande_net,0) 
		     else 0 
			end 
		  ) as mtt_total_net,
		  SUM( 
		     case 
		     when (last_statut!='ANNUL') then IFNULL(mvm.mtt_commande,0) 
		     else 0 
			end 
		  ) as mtt_total, 
			SUM( 
		     case 
		     when (last_statut!='ANNUL') then IFNULL(mvm.mtt_reduction,0) 
		     else 0 
			end 
		  ) as mtt_reduction, 
		  SUM( 
		     case 
		     when (last_statut!='ANNUL') then IFNULL(mvm.mtt_art_reduction,0) 
		     else 0 
			end 
		  ) as mtt_art_reduction,
		  SUM( 
		     case 
		     when (last_statut!='ANNUL') then IFNULL(mvm.mtt_art_offert,0) 
		     else 0 
			end 
		  ) as mtt_art_offert,
		  
		  SUM( 
		     case 
		     when (last_statut!='ANNUL') then IFNULL(mvm.mtt_donne_point,0) 
		     else 0 
			end 
		  ) as mtt_donne_point,
		  SUM( 
		     case 
		     when (last_statut!='ANNUL') then IFNULL(mvm.mtt_portefeuille,0) 
		     else 0 
			end 
		  ) as mtt_portefeuille,
		  SUM( 
		     case 
		     when (last_statut!='ANNUL') then IFNULL(mvm.mtt_marge_caissier,0) 
		     else 0 
			end 
		  ) as mtt_marge_caissier,
		count(0) as nbr_vente,
		mvm.etablissement_id,
		mvm.societe_id, mvm.abonne_id
		from caisse_mouvement mvm 
		inner join caisse_journee cj on cj.id=mvm.caisse_journee_id
		inner join journee jour on jour.id=cj.journee_id
		inner join caisse cai on cj.caisse_id=cai.id 
		left join user us on us.id=cj.user_id
	    group by cj.id;

create or replace SQL SECURITY INVOKER view journee_vente_view as 
	select 	jour.id as journee_id,
			jour.date_journee,
			jour.statut_journee,
			SUM(cvv.nbr_livraison) as nbr_livraison,
			SUM(cvv.mtt_ouverture) as mtt_ouverture,
			SUM(cvv.mtt_cloture_caissier) as mtt_cloture_caissier,
			SUM(cvv.mtt_cloture_caissier_espece) as mtt_cloture_caissier_espece,
			SUM(cvv.mtt_cloture_caissier_cb) as mtt_cloture_caissier_cb,
			SUM(cvv.mtt_cloture_caissier_cheque) as mtt_cloture_caissier_cheque,
			SUM(cvv.mtt_cloture_caissier_dej) as mtt_cloture_caissier_dej,
			SUM(cvv.mtt_cb) as mtt_cb, 
			SUM(cvv.mtt_cheque) as mtt_cheque,
			SUM(cvv.mtt_espece) as mtt_espece,
			SUM(cvv.mtt_dej) as mtt_dej, 
			SUM(cvv.mtt_annule) as mtt_annule, 
			SUM(cvv.mtt_annul_ligne) as mtt_annul_ligne,
			SUM(cvv.mtt_total_net) as mtt_total_net,
			SUM(cvv.mtt_total) as mtt_total, 
			SUM(cvv.mtt_reduction) as mtt_reduction, 
			SUM(cvv.mtt_art_reduction) as mtt_art_reduction, 
			SUM(cvv.mtt_art_offert) as mtt_art_offert,
			
			SUM(cvv.mtt_donne_point) as mtt_donne_point,
			SUM(cvv.mtt_portefeuille) as mtt_portefeuille,
			
			SUM(cvv.nbr_vente) as nbr_vente,
			cvv.etablissement_id,
			cvv.societe_id, cvv.abonne_id
		from journee jour 
		left join caisse_vente_view cvv on jour.id=cvv.journee_id
	    group by jour.id;

	    
create or replace SQL SECURITY INVOKER view mouvement_stock_view as 
	select 
			mvm.id as mouvement_id,
			mvm.date_mouvement,
			mvm.num_bl,
			mvm.num_facture,
			mvm.mouvement_group_id,
			mvm.is_groupant,
			mvm.type_mvmnt,
			concat(fourn.code,' - ',fourn.libelle) as fourn_lib,
			fourn.id as fourn_id,
			dest.titre as destination, 
			empl.titre as emplacement,
			
			sum( IFNULL(mvmart.prix_ht,0)*IFNULL(mvmart.quantite,0)) as montant_ht,
		 	sum( 
		 		((IFNULL(mvmart.prix_ht,0)*IFNULL(mvmart.quantite,0))-(((IFNULL(mvmart.prix_ht,0)*IFNULL(mvmart.quantite,0))*IFNULL(remise,0))/100))+
				((((IFNULL(mvmart.prix_ht,0)*IFNULL(mvmart.quantite,0))-(((IFNULL(mvmart.prix_ht,0)*IFNULL(mvmart.quantite,0))*IFNULL(remise,0))/100))*tva.libelle)/100)
			) as montant_ttc,
			sum(((((IFNULL(mvmart.prix_ht,0)*IFNULL(mvmart.quantite,0))-(((IFNULL(mvmart.prix_ht,0)*IFNULL(mvmart.quantite,0))*IFNULL(remise,0))/100))*tva.libelle)/100)) as montant_tva,
			mvm.etablissement_id,
			mvm.societe_id, mvm.abonne_id
        from mouvement mvm left join mouvement_article mvmart on mvm.id=mvmart.mouvement_id
		left join val_type_enumere tva on tva.id=mvmart.tva_enum
		left join emplacement empl on empl.id=mvm.emplacement_id
		left join emplacement dest on dest.id=mvm.destination_id
		left join fournisseur fourn on fourn.id=mvm.fournisseur_id
		group by mvm.id;	    
