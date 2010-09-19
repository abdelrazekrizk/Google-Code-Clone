<?php

/**
 * Account filter form base class.
 *
 * @package    gtonline
 * @subpackage filter
 * @author     Your name here
 * @version    SVN: $Id: sfDoctrineFormFilterGeneratedTemplate.php 29570 2010-05-21 14:49:47Z Kris.Wallsmith $
 */
abstract class BaseAccountFormFilter extends BaseFormFilterDoctrine
{
  public function setup()
  {
    $this->setWidgets(array(
      'email'     => new sfWidgetFormFilterInput(array('with_empty' => false)),
      'password'  => new sfWidgetFormFilterInput(array('with_empty' => false)),
      'firstname' => new sfWidgetFormFilterInput(array('with_empty' => false)),
      'lastname'  => new sfWidgetFormFilterInput(array('with_empty' => false)),
      'isAdmin'   => new sfWidgetFormChoice(array('choices' => array('' => 'yes or no', 1 => 'yes', 0 => 'no'))),
    ));

    $this->setValidators(array(
      'email'     => new sfValidatorPass(array('required' => false)),
      'password'  => new sfValidatorPass(array('required' => false)),
      'firstname' => new sfValidatorPass(array('required' => false)),
      'lastname'  => new sfValidatorPass(array('required' => false)),
      'isAdmin'   => new sfValidatorChoice(array('required' => false, 'choices' => array('', 1, 0))),
    ));

    $this->widgetSchema->setNameFormat('account_filters[%s]');

    $this->errorSchema = new sfValidatorErrorSchema($this->validatorSchema);

    $this->setupInheritance();

    parent::setup();
  }

  public function getModelName()
  {
    return 'Account';
  }

  public function getFields()
  {
    return array(
      'id'        => 'Number',
      'email'     => 'Text',
      'password'  => 'Text',
      'firstname' => 'Text',
      'lastname'  => 'Text',
      'isAdmin'   => 'Boolean',
    );
  }
}
