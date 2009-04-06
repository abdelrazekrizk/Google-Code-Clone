<?php

/**
 * adminRoster actions.
 *
 * @package    epgp
 * @subpackage adminRoster
 * @author     Your name here
 * @version    SVN: $Id: actions.class.php 12474 2008-10-31 10:41:27Z fabien $
 */
class adminRosterActions extends sfActions
{
  public function executeIndex(sfWebRequest $request)
  {
    $c = new Criteria();
    $c->addAscendingOrderByColumn(RosterPeer::NAME);
    $this->roster_list = RosterPeer::doSelect($c);
  }

  public function executeNew(sfWebRequest $request)
  {
    $this->classes = ClassesPeer::doSelect(new Criteria());
    $this->races = RacesPeer::doSelect(new Criteria());
  }

  public function executeCreate(sfWebRequest $request)
  {
    $this->forward404Unless($request->isMethod('put'));
    $char = new Roster();
    $char->setName($request->getParameter('name'));
    $char->setCharclass($request->getParameter('class'));
    $char->setCharrace($request->getParameter('race'));
    $char->setJoinedOn(strtotime($request->getParameter('joinedonMonth').'/'.$request->getParameter('joinedonDay').'/'.$request->getParameter('joinedonYear')));
    $char->setEp(0);
    $char->setGp(0);
    $char->setPriority(0);
    $char->save();
    $this->forward('adminRoster', 'index');
  }

  public function executeEdit(sfWebRequest $request)
  {
    $this->forward404Unless($roster = RosterPeer::retrieveByPk($request->getParameter('id')), sprintf('Object roster does not exist (%s).', $request->getParameter('id')));
    if(!$roster->getJoinedOn()){
      $roster->setJoinedOn(strtotime('today'));
    }
    $this->char = $roster;
    $this->classes = ClassesPeer::doSelect(new Criteria());
    $this->races = RacesPeer::doSelect(new Criteria());
  }
  
  public function executeUpdate(sfWebRequest $request){
    $this->forward404Unless($request->isMethod('post') || $request->isMethod('put'));
    $this->forward404Unless($roster = RosterPeer::retrieveByPk($request->getParameter('id')), sprintf('Object roster does not exist (%s).', $request->getParameter('id')));
    $this->form = new RosterForm($roster);
    
    $this->processForm($request, $this->form);
    
    if($this->form->isValid()){
      $this->redirect('adminRoster', 'index');
    } else {
      $this->setTemplate('edit');
    };
    
  }

  public function executeDelete(sfWebRequest $request)
  {
    $request->checkCSRFProtection();

    $this->forward404Unless($roster = RosterPeer::retrieveByPk($request->getParameter('id')), sprintf('Object roster does not exist (%s).', $request->getParameter('id')));
    $roster->delete();

    $this->redirect('adminRoster/index');
  }

  protected function processForm(sfWebRequest $request, sfForm $form)
  {
    $form->bind($request->getParameter($form->getName()), $request->getFiles($form->getName()));
    if ($form->isValid())
    {
      $roster = $form->save();

      $this->redirect('adminRoster/edit?id='.$roster->getId());
    }
  }
}
