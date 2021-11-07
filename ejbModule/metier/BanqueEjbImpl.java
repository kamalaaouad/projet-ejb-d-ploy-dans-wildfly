package metier;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import metier.entities.Compte;

@Stateless(name = "BK")
public class BanqueEjbImpl implements IBanqueLocal,IBanqueRemote {
	
	@PersistenceContext(name = "BanqueCompteEjb")
	private EntityManager em;

	@Override
	public Compte createCompte(Compte c) {
		em.persist(c);
		return c;
	}

	@Override
	public Compte findCompteByCode(Long code) {
		Compte cp = em.find(Compte.class, code);
		if(cp == null) throw new RuntimeException("le compte est introuvable");
		return cp;
	}

	@Override
	public List<Compte> findListCompte() {
		Query query = em.createQuery("select c from Compte c");
		return query.getResultList();
	}

	@Override
	public void verser(Long code, double montant) {
		Compte cp = findCompteByCode(code);
		cp.setSolde(cp.getSolde()+montant);
	}

	@Override
	public void retirer(Long code, double montant) {
		Compte cp = findCompteByCode(code);
		if(cp.getSolde()<montant) throw new RuntimeException("le montant est insuffisant");
		cp.setSolde(cp.getSolde() - montant);
	}

	@Override
	public void verement(Long code1, Long code2, double montant) {
		retirer(code1, montant);
		verser(code2, montant);
	}

}
