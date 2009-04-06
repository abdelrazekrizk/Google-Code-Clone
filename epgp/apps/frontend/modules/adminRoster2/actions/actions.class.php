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
    $this->roster_list = RosterPeer::doSelect(new Criteria());
  }

  public function executeNew(sfWebRequest $request)
  {
    $this->form = new RosterForm();
  }

  public function executeCreate(sfWebRequest $request)
  {
    $this->forward404Unless($request->isMethod('post'));

    $this->form = new RosterForm();

    $this->processForm($request, $this->form);

    $this->setTemplate('new');
  }

  public function executeEdit(sfWebRequest $request)
  {
    $this->forward404Unless($roster = RosterPeer::retrieveByPk($request->getParameter('id')), sprintf('Object roster does not exist (%s).', $request->getParameter('id')));
    if(!$roster->getJoinedOn()){
      $roster->setJoinedOn(strtotime('today'));
    }
    $this->form = new RosterForm($roster);
  }

  public function executeUpdate(sfWebRequest $request)
  {
    $this->forward404Unless($request->isMethod('post') || $request->isMethod('put'));
    $this->forward404Unless($roster = RosterPeer::retrieveByPk($request->getParameter('id')), sprintf('Object roster does not exist (%s).', $request->getParameter('id')));
    $this->form = new RosterForm($roster);

    $this->processForm($request, $this->form);

    $this->setTemplate('edit');
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
